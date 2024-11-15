package com.avanza.license.service.impl;

import com.avanza.license.Dto.DataTransferDTO;
import com.avanza.license.Dto.ModuleDto;
import com.avanza.license.Enum.ErrorCode;
import com.avanza.license.entity.*;
import com.avanza.license.repositories.*;
import com.avanza.license.service.UserSubscriptionPlanService;
import com.avanza.license.util.ErrorHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserSubscriptionPlanServiceImpl implements UserSubscriptionPlanService {

    @Autowired
    private UserSubscriptionPlanRepository userSubscriptionPlanRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private LicenseKeyRepository licenseKeyRepository;

    @Autowired
    private ApplicationRepository applicationRepository;
    @Value("${client.id}")
    private String clientId;

    @Autowired
    private UserLicenseRepository userLicenseRepository;
    @Lazy
    @Autowired
    private UserSubscriptionPlanServiceImpl self;

    @Override
    public List<UserSubscriptionPlan> getAllUserSubscriptionPlans() {
        return userSubscriptionPlanRepository.findAll();
    }

    @Override
    public DataTransferDTO updateUserSubscriptionPlanByUserIdAndAppIdAndSubName(Long userId, Long appId, Long subscriptionId) {
        Optional<UserSubscriptionPlan> userSubscriptionPlanOptonal = userSubscriptionPlanRepository.findByUserTableUserIdAndApplicationAppId(userId, appId);
        if (!userSubscriptionPlanOptonal.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID_APP_ID);
        }
        UserSubscriptionPlan userSubscriptionPlan = userSubscriptionPlanOptonal.get();
        Optional<SubscriptionPlan> subscriptionOptional = subscriptionPlanRepository.findBySubscriptionId(subscriptionId);
        if (subscriptionOptional.isPresent()) {
            SubscriptionPlan subscriptionPlan = subscriptionOptional.get();

            Timestamp startDate;
            Timestamp endDate;

            if ("general".equalsIgnoreCase(subscriptionPlan.getSubscriptionType()) ||
                    "General".equalsIgnoreCase(subscriptionPlan.getSubscriptionType())) {
                LocalDate localStartDate = LocalDate.now();
                LocalDate localEndDate = localStartDate.plusMonths(subscriptionPlan.getDurationMonths());
                startDate = Timestamp.valueOf(localStartDate.atStartOfDay());
                endDate = Timestamp.valueOf(localEndDate.atStartOfDay());
            } else {
                startDate = subscriptionPlan.getStartDate();
                endDate = subscriptionPlan.getEndDate();
            }

            userSubscriptionPlan.setSubscriptionPlan(subscriptionPlan);
            userSubscriptionPlan.setUpdatedOn(new Date());
            userSubscriptionPlan.setCreatedOn(new Date());
            userSubscriptionPlan.setUpdatedBy("System");
            userSubscriptionPlan.setCreatedBy("System");
            userSubscriptionPlan.setStartDate(startDate);
            userSubscriptionPlan.setEndDate(endDate);
            UserSubscriptionPlan savedUserSubscriptionPlan= userSubscriptionPlanRepository.save(userSubscriptionPlan);

            LicenseKey savedLicenseKey = null;
            Application application = null;
            UserLicense userLicenseUpdate = null;
            String previousKey = "";
            Optional<Application> applicationOptional = applicationRepository.findByAppId(appId);
            if (applicationOptional.isPresent()) {
                application = applicationOptional.get();
                application.setSubscriptionPlan(subscriptionPlan);
                applicationRepository.save(application);
                Optional<LicenseKey> optionalLicenseKeyValue = licenseKeyRepository.findByApplicationAppId(appId);
                if (optionalLicenseKeyValue.isPresent()) {
                    LicenseKey keyValue = optionalLicenseKeyValue.get();
                    previousKey = keyValue.getKeyValue();
                }
                savedLicenseKey = updateGenerateLicenseKey(appId, application, endDate);
                userLicenseUpdate = self.updatedLicenseUser(userId, appId, previousKey, application, savedLicenseKey, application.getOwner());
            }
            //Saved audit log for updation of user subscriptionPlan
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("UPDATE");
            auditLog.setEntityName("UserSubscriptionPlan");
            auditLog.setEntityId(savedUserSubscriptionPlan.getUserSubscriptionId());
            auditLog.setCreatedOn(new Date());
            auditLog.setCreatedBy(savedUserSubscriptionPlan.getCreatedBy()); // Assuming you set `createdBy` during user registration
            auditLog.setDetails("UserSubscriptionPlan registered");
            auditLogRepository.save(auditLog);

            return getDataTransferDTO(savedLicenseKey, application);
        } else {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_SUBSCRIPTION_ID);
            return null;
        }
    }

    private static DataTransferDTO getDataTransferDTO(LicenseKey savedLicenseKey, Application application) {
        DataTransferDTO dataTransferDTO = new DataTransferDTO();
        dataTransferDTO.setUserId(savedLicenseKey.getApplication().getOwner().getUserId());
        dataTransferDTO.setUsername(savedLicenseKey.getApplication().getOwner().getUsername());
        dataTransferDTO.setEmail(savedLicenseKey.getApplication().getOwner().getEmail());
        dataTransferDTO.setAppId(savedLicenseKey.getApplication().getAppId());
        dataTransferDTO.setAppName(savedLicenseKey.getApplication().getAppName());
        dataTransferDTO.setDescription(savedLicenseKey.getApplication().getDescription());
        dataTransferDTO.setLicenseId(savedLicenseKey.getLicenseId());
        dataTransferDTO.setKeyValue(savedLicenseKey.getKeyValue());

        List<ModuleDto> moduleDTOs = application.getModules().stream()
                .map(module -> new ModuleDto(module.getModuleId(), module.getApplicationName(), module.getName(), module.getIdentificationKey()))
                .collect(Collectors.toList());

        dataTransferDTO.setModules(moduleDTOs);
        return dataTransferDTO;
    }

    private LicenseKey updateGenerateLicenseKey(Long appId, Application application, Timestamp expiryDate) {
        Optional<LicenseKey> licenseKeyOptional = licenseKeyRepository.findByApplicationAppId(appId);
        if (licenseKeyOptional.isPresent()) {
            LicenseKey licenseKey = licenseKeyOptional.get();
            try {
                SecretKey secretKey = generateSecretKey();
                String encryptedKey = encryptLicenseKey(clientId, secretKey);
                licenseKey.setKeyValue(encryptedKey);
                licenseKey.setIsActive(true);
                licenseKey.setExpirationDate(expiryDate);
                licenseKey.setApplication(application);
                licenseKey.setCreatedOn(new Date());
                licenseKey.setCreatedBy("system");
                licenseKey.setUpdatedOn(new Date());
                licenseKey.setUpdatedBy("System");

                return licenseKeyRepository.save(licenseKey);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_APP_ID);
            return null;
        }
    }

//    @CachePut(value = "licenseCache", key = "#userId + '-' + #appId")
    public UserLicense updatedLicenseUser(Long userId, Long appId, String keyValue, Application application, LicenseKey savedLicenseKey, UserTable owner) {
        Optional<UserLicense> optionalUserLicense = userLicenseRepository.findByUserTableUserIdAndApplicationAppId(userId, appId);
        if (optionalUserLicense.isPresent()) {
            UserLicense userLicense = optionalUserLicense.get();
            try {
                userLicense.setUserTable(owner);
                userLicense.setApplication(application);
                userLicense.setLicenseKey(savedLicenseKey);
                userLicense.setCreatedOn(new Date());
                userLicense.setCreatedBy("Admin");
                userLicense.setUpdatedOn(new Date());
                userLicense.setUpdatedBy("Admin");
                return userLicenseRepository.save(userLicense);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }

    private String encryptLicenseKey(String licenseKey, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(licenseKey.getBytes());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(encryptedBytes);
    }

    @Override
    public UserSubscriptionPlan getUserSubscriptionByUserIdAndAppId(Long userId, Long appId) {
        Optional<UserSubscriptionPlan> userSubscriptionPlanOptional = userSubscriptionPlanRepository.findByUserTableUserIdAndApplicationAppId(userId, appId);
        if (userSubscriptionPlanOptional.isPresent()) {
            return userSubscriptionPlanOptional.get();
        } else {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID_APP_ID);
            return null;
        }
    }

    @Override
    public List<UserSubscriptionPlan> getAllUserSubscriptionPlanByUserId(Long userId) {
        return userSubscriptionPlanRepository.findByUserTableUserId(userId);
    }

}