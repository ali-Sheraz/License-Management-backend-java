package com.avanza.license.service.impl;

import com.avanza.license.Dto.DataTransferDTO;
import com.avanza.license.Dto.ModuleDto;
import com.avanza.license.Enum.ErrorCode;
import com.avanza.license.entity.*;
import com.avanza.license.entity.Module;
import com.avanza.license.repositories.*;
import com.avanza.license.service.ApplicationService;
import com.avanza.license.util.ErrorHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserTableRepository userTableRepository;
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Value("${client.id}")
    private String clientId;

    @Autowired
    private LicenseKeyRepository licenseKeyRepository;

    @Autowired
    private UserLicenseRepository userLicenseRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private UserSubscriptionPlanRepository userSubscriptionPlanRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private UserModuleRepository userModuleRepository;

    @Override
    public DataTransferDTO saveApplication(Application app) {
        long userId = app.getOwner().getUserId();
        Optional<UserTable> ownerOptional = userTableRepository.findByUserId(userId);
        if (!ownerOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID);
        }
        UserTable owner = ownerOptional.get();
        app.setOwner(owner);
        app.setCreatedOn(new Date());
        app.setCreatedBy("system");
        app.setUpdatedOn(new Date());
        app.setUpdatedBy("System");
        app.setIsActive(true);

        long subscriptionId = app.getSubscriptionPlan().getSubscriptionId();
        Optional<SubscriptionPlan> subscriptionPlanOptional = subscriptionPlanRepository.findBySubscriptionId(subscriptionId);
        if (!subscriptionPlanOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_SUBSCRIPTION_ID);
        }
        SubscriptionPlan subscriptionPlan = subscriptionPlanOptional.get();
        app.setSubscriptionPlan(subscriptionPlan);
        Timestamp startDate;
        Timestamp endDate;
        LocalDate localStartDate = LocalDate.now();
        LocalDate localEndDate = localStartDate.plusMonths(subscriptionPlan.getDurationMonths());
        startDate = Timestamp.valueOf(localStartDate.atStartOfDay());
        endDate = Timestamp.valueOf(localEndDate.atStartOfDay());
        app.setExpirationDate(endDate);

        List<Module> modules = app.getModules();
        if (modules == null || modules.isEmpty()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_MODULE_ID);
        }

        List<Module> validModules = modules.stream()
                .map(module -> moduleRepository.findByModuleId(module.getModuleId())
                        .orElseThrow(() -> new RuntimeException("Invalid module ID: " + module.getModuleId())))
                .collect(Collectors.toList());

        app.setModules(validModules);

        Application savedApplication = applicationRepository.save(app);

        UserSubscriptionPlan userSubscriptionPlan = saveUserSubscriptionPlan(owner, savedApplication, subscriptionPlan);
        LicenseKey savedLicenseKey = generateLicenseKey(savedApplication, userSubscriptionPlan.getEndDate());
        if (savedLicenseKey == null) {
            ErrorHandlerUtil.handleError(ErrorCode.LICENSE_GENERATION_FAILED);
        }

        UserLicense savedUserLicense = saveUserLicense(savedApplication, owner, savedLicenseKey);
        validModules.forEach(module -> saveUserModule(owner, savedApplication, module));

        // Log the action in the AuditLog
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("REGISTER");
        auditLog.setEntityName("Application");
        auditLog.setEntityId(savedApplication.getAppId());
        auditLog.setCreatedOn(new Date());
        auditLog.setCreatedBy(app.getCreatedBy()); // Assuming you set `createdBy` during user registration
        auditLog.setDetails("Application Registered");

        auditLogRepository.save(auditLog); // Save the audit log entry
        return getDataTransferDTO(savedUserLicense, validModules);
    }

    private static DataTransferDTO getDataTransferDTO(UserLicense savedUserLicense, List<Module> modules) {
        DataTransferDTO dataTransferDTO = new DataTransferDTO();
        dataTransferDTO.setUserId(savedUserLicense.getUserTable().getUserId());
        dataTransferDTO.setUsername(savedUserLicense.getUserTable().getUsername());
        dataTransferDTO.setEmail(savedUserLicense.getUserTable().getEmail());
        dataTransferDTO.setAppId(savedUserLicense.getApplication().getAppId());
        dataTransferDTO.setAppName(savedUserLicense.getApplication().getAppName());
        dataTransferDTO.setDescription(savedUserLicense.getApplication().getDescription());
        dataTransferDTO.setKeyValue(savedUserLicense.getLicenseKey().getKeyValue());
        dataTransferDTO.setLicenseId(savedUserLicense.getLicenseKey().getLicenseId());

        List<ModuleDto> moduleDTOs = modules.stream()
                .map(module -> new ModuleDto(module.getModuleId(), module.getApplicationName(), module.getName(), module.getIdentificationKey()))
                .collect(Collectors.toList());

        dataTransferDTO.setModules(moduleDTOs);
        return dataTransferDTO;
    }

    private UserModule saveUserModule(UserTable userTable, Application application, Module module) {
        UserModule userModule = new UserModule();
        userModule.setUserTable(userTable);
        userModule.setApplication(application);
        userModule.setModule(module);
        userModule.setUpdatedOn(new Date());
        userModule.setCreatedOn(new Date());
        userModule.setUpdatedBy("System");
        userModule.setCreatedBy("System");
        return userModuleRepository.save(userModule);
    }

    private LicenseKey generateLicenseKey(Application application, Timestamp expiryDate) {
        try {
//            SecretKey secretKey = generateSecretKey();
//            String encryptedKey = encryptLicenseKey(clientId, secretKey);

            String encryptedKey = application.getBiosId() + application.getFqdn();
            String generatedHexKey=generateSha256Hash(encryptedKey);

            LicenseKey licenseKey = new LicenseKey();
            licenseKey.setKeyValue(generatedHexKey);
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
    }

    private UserSubscriptionPlan saveUserSubscriptionPlan(UserTable userTable, Application application, SubscriptionPlan subscriptionPlan) {
        try {
            Timestamp startDate;
            Timestamp endDate;

            if ("general".equalsIgnoreCase(subscriptionPlan.getSubscriptionType()) ||
                    "monthly".equalsIgnoreCase(subscriptionPlan.getSubscriptionType()) ||
                    "yearly".equalsIgnoreCase(subscriptionPlan.getSubscriptionType())) {
                LocalDate localStartDate = LocalDate.now();
                LocalDate localEndDate = localStartDate.plusMonths(subscriptionPlan.getDurationMonths());
                startDate = Timestamp.valueOf(localStartDate.atStartOfDay());
                endDate = Timestamp.valueOf(localEndDate.atStartOfDay());
            } else {
                startDate = subscriptionPlan.getStartDate();
                endDate = subscriptionPlan.getEndDate();
            }

            UserSubscriptionPlan userSubscriptionPlan = new UserSubscriptionPlan();
            userSubscriptionPlan.setUserTable(userTable);
            userSubscriptionPlan.setApplication(application);
            userSubscriptionPlan.setSubscriptionPlan(subscriptionPlan);
            userSubscriptionPlan.setStartDate(startDate);
            userSubscriptionPlan.setEndDate(endDate);
            userSubscriptionPlan.setCreatedOn(new Date());
            userSubscriptionPlan.setCreatedBy("system");
            userSubscriptionPlan.setUpdatedOn(new Date());
            userSubscriptionPlan.setUpdatedBy("System");

            return userSubscriptionPlanRepository.save(userSubscriptionPlan);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private UserLicense saveUserLicense(Application application, UserTable owner, LicenseKey licenseKey) {
        try {
            UserLicense userLicense = new UserLicense();
            userLicense.setUserTable(owner);
            userLicense.setApplication(application);
            userLicense.setLicenseKey(licenseKey);
            userLicense.setCreatedOn(new Date());
            userLicense.setCreatedBy("system");
            userLicense.setUpdatedOn(new Date());
            userLicense.setUpdatedBy("System");
            return userLicenseRepository.save(userLicense);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Application getAppById(Long appId) {
        Optional<Application> applicationOptional = applicationRepository.findByAppId(appId);
        if (!applicationOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_APP_ID);
        }
        return applicationOptional.get();
    }

    @Override
    public Application updateApplication(Long appId, Application updatedApp) {
        Optional<Application> existingAppOptional = applicationRepository.findByAppId(appId);
        if (!existingAppOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_APP_ID);
        }
        Application existingApp = existingAppOptional.get();
//        existingApp.setAppName(updatedApp.getAppName());
        existingApp.setDescription(updatedApp.getDescription());
        existingApp.setMaxUsers(updatedApp.getMaxUsers());
        existingApp.setIsActive(true);
        existingApp.setUpdatedOn(new Date());
        existingApp.setUpdatedBy(updatedApp.getUpdatedBy());
        existingApp.setCreatedOn(new Date());
        existingApp.setCreatedBy(updatedApp.getUpdatedBy());

        List<Module> existingModules = existingApp.getModules();
        List<Module> newModules = updatedApp.getModules();

        if (newModules != null && !newModules.isEmpty()) {
            for (Module newModule : newModules) {
                Optional<Module> moduleOptional = moduleRepository.findByModuleId(newModule.getModuleId());
                if (!moduleOptional.isPresent()) {
                    ErrorHandlerUtil.handleError(ErrorCode.INVALID_MODULE_ID); // Or handle the error accordingly
                }
                Module module = moduleOptional.get();
                if (existingModules.stream().anyMatch(existingModule -> existingModule.getModuleId().equals(module.getModuleId()))) {
                    ErrorHandlerUtil.handleError(ErrorCode.MODULE_ALREADY_EXISTS); // Or handle the error accordingly
                } else {
                    existingModules.add(module);
                    saveUserModule(existingApp.getOwner(), existingApp, module);
                }
            }
        }
        existingApp.setModules(existingModules);
        Application upApp= applicationRepository.save(existingApp);
        // Log the action in the AuditLog
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("Update");
        auditLog.setEntityName("Application");
        auditLog.setEntityId(upApp.getAppId());
        auditLog.setCreatedOn(new Date());
        auditLog.setCreatedBy(upApp.getCreatedBy()); // Assuming you set `createdBy` during user registration
        auditLog.setDetails("Application Updated");

        auditLogRepository.save(auditLog); // Save the audit log entry
        return upApp;

    }

    @Override
    @Transactional
    public void deleteApplication(Long appId) {
        if (applicationRepository.existsByAppId(appId)) {
            applicationRepository.deleteByAppId(appId);
        } else {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_APP_ID);
        }
    }

    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }
    @Override
    public List<Application> findApplicationsByUsername(String username) {
        return applicationRepository.findByOwnerUsername(username);
    }
    @Override
    public List<Application> getAppByUserId(Long userId) {
        return applicationRepository.findByOwnerUserId(userId);
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

    private String generateSha256Hash(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes("UTF-8"));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
