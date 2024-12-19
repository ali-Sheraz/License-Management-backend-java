package com.avanza.license.service.impl;

import com.avanza.license.Dto.*;
import com.avanza.license.Enum.ErrorCode;
import com.avanza.license.entity.*;
import com.avanza.license.entity.Module;
import com.avanza.license.repositories.*;
import com.avanza.license.service.UserLicenseService;
import com.avanza.license.util.ErrorHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserLicenseServiceImpl implements UserLicenseService {

    @Autowired
    private UserLicenseRepository userLicenseRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private UserTableRepository userTableRepository;


    @Override
    public List<UserLicense> getAllUserLicenses() {
        return userLicenseRepository.findAll();
    }

    @Override
    public UserLicense getLicenseByUserIdAndAppId(Long userId, Long appId) {
        Optional<UserLicense> userLicenseOptional = userLicenseRepository.findByUserTableUserIdAndApplicationAppId(userId, appId);
        if (!userLicenseOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID_APP_ID);
        }
        return userLicenseOptional.get();
    }
    /**
     * Above method for login from client through userId appId and keyValue
     * below method is through userId and appId
     * @return Inserted EAVs
     */
//    @Override
////    @Cacheable(value = "licenseCache", key = "#userId + '-' + #appId")
//    public UserLicenseFloatAbleDTO isLicenseKeyValid(Long userId, Long appId, String loginId, String keyValue) {
//        Optional<UserLicense> userLicenseAllOptional = userLicenseRepository.findByUserTableUserIdAndApplicationAppIdAndLicenseKeyKeyValue(userId, appId, keyValue);
//
//        if (userLicenseAllOptional.isEmpty()) {
//            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID_APP_ID_LICENSE_KEY);
//        }
//
//        UserLicense userLicense = userLicenseAllOptional.get();
//        Application application = userLicense.getApplication();
//        int maxUsers = application.getMaxUsers();
//
//        // Convert the Timestamp to LocalDate
//        LocalDate currentDate = LocalDate.now();
//        LocalDate expiryDate = userLicense.getLicenseKey().getExpirationDate().toLocalDateTime().toLocalDate();
//
////        if (currentDate.isAfter(expiryDate)) {
////            ErrorHandlerUtil.handleError(ErrorCode.LICENSE_KEY_EXPIRED);
////        }
//
//        long userSessionCount = userSessionRepository.countByApplicationAppId(appId);
//
//        if (userSessionCount > maxUsers) {
//            ErrorHandlerUtil.handleError(ErrorCode.MAX_USER_REACHED);
//        }
//
//        if (userSessionCount == maxUsers && !userSessionRepository.existsBySessionIdAndApplicationAppId(loginId, appId)) {
//            ErrorHandlerUtil.handleError(ErrorCode.MAX_USER_REACHED);
//        }
//
//        return getUserLicenseFloatAbleDTO(userLicense);
//    }

    /**
     * below method is through userId and appId
     */
    @Override
    public String isLicenseKeyValid(Long userId, Long appId, String loginId) {
        Optional<UserLicense> userLicenseAllOptional = userLicenseRepository.findByUserTableUserIdAndApplicationAppId(userId, appId);

        if (userLicenseAllOptional.isEmpty()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID_APP_ID);
        }

        UserLicense userLicense = userLicenseAllOptional.get();
        Application application = userLicense.getApplication();
        int maxUsers = application.getMaxUsers();


        long userSessionCount = userSessionRepository.countByApplicationAppId(appId);

        if (userSessionCount > maxUsers) {
            ErrorHandlerUtil.handleError(ErrorCode.MAX_USER_REACHED);
        }

        if (userSessionCount == maxUsers && !userSessionRepository.existsBySessionIdAndApplicationAppId(loginId, appId)) {
            ErrorHandlerUtil.handleError(ErrorCode.MAX_USER_REACHED);
        }

        return "Valid User";
    }
    /**
     * below method is when unison start he hit request on it and get data from it and set in
     * unison redis
     */
    @Override
    public UserLicenseFloatAbleDTO isLicenseKeyValidForFloatAbleMatrix(LicenseRequestParam licenseRequestParam) {
        long userId = licenseRequestParam.getUserId();
        long appId = licenseRequestParam.getAppId();
        String keyValue = licenseRequestParam.getKeyValue();
        Optional<UserLicense> userLicenseAllOptional = userLicenseRepository.findByUserTableUserIdAndApplicationAppIdAndLicenseKeyKeyValue(userId, appId, keyValue);

        if (userLicenseAllOptional.isEmpty()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID_APP_ID_LICENSE_KEY);
        }

        UserLicense userLicense = userLicenseAllOptional.get();
        Application application = userLicense.getApplication();
        int maxUsers = application.getMaxUsers();

        // Convert the Timestamp to LocalDate
        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = userLicense.getLicenseKey().getExpirationDate().toLocalDateTime().toLocalDate();

        if (currentDate.isAfter(expiryDate)) {
            ErrorHandlerUtil.handleError(ErrorCode.LICENSE_KEY_EXPIRED);
        }
        long userSessionCount = userSessionRepository.countByApplicationAppId(appId);

        if (userSessionCount > maxUsers) {
            ErrorHandlerUtil.handleError(ErrorCode.MAX_USER_REACHED);
        }
        return getUserLicenseFloatAbleDTO(userLicense);
    }

    private UserLicenseFloatAbleDTO getUserLicenseFloatAbleDTO(UserLicense userLicense) {
        UserLicenseFloatAbleDTO dto = new UserLicenseFloatAbleDTO();
        dto.setUserLicenseId(userLicense.getUserLicenseId());
        dto.setUserId(userLicense.getUserTable().getUserId());
        dto.setUsername(userLicense.getUserTable().getUsername());
        dto.setEmail(userLicense.getUserTable().getEmail());
        dto.setAppId(userLicense.getApplication().getAppId());
        dto.setAppName(userLicense.getApplication().getAppName());
        dto.setMaxUsers((long) userLicense.getApplication().getMaxUsers());
        dto.setLicenseId(userLicense.getLicenseKey().getLicenseId());
        dto.setKeyValue(userLicense.getLicenseKey().getKeyValue());
        dto.setExpirationDate(Timestamp.valueOf(userLicense.getLicenseKey().getExpirationDate().toLocalDateTime()));

        List<ModuleDto> moduleDTOs = userLicense.getApplication().getModules().stream()
                .map(module -> new ModuleDto(module.getModuleId(), module.getApplicationName(), module.getName(), module.getIdentificationKey()))
                .collect(Collectors.toList());
        dto.setModules(moduleDTOs);

        return dto;
    }

    @Override
    public SessionDTO insertingSession(Long userId, Long appId, String loginId) {
        UserSession userSession = new UserSession();
        Optional<UserLicense> userLicenseAllOptional = userLicenseRepository.findByUserTableUserIdAndApplicationAppId(userId, appId);
        UserLicense userLicense = userLicenseAllOptional.get();
        Application application = userLicense.getApplication();
        UserTable userTable = userLicense.getUserTable();
        userSession.setSessionId(loginId);
        userSession.setUserTable(userTable);
        userSession.setApplication(application);
        userSession.setUpdatedOn(new Date());
        userSession.setCreatedOn(new Date());
        userSession.setUpdatedBy("System");
        userSession.setCreatedBy("System");
        userSession.setStartTime(LocalDateTime.now());
        userSession.setEndTime(LocalDateTime.now());
        userSession = userSessionRepository.save(userSession);
        return getDataTransferDTO(userSession);
    }

    private static SessionDTO getDataTransferDTO(UserSession userSession) {
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setAppId(userSession.getApplication().getAppId());
        sessionDTO.setSessionId(userSession.getSessionId());
        sessionDTO.setStartTime(userSession.getStartTime());
        sessionDTO.setEndTime(userSession.getEndTime());
        sessionDTO.setUserId(userSession.getUserTable().getUserId());
        return sessionDTO;
    }

    @Override
    public List<UserLicense> getUserLicenseByUserId(Long userId) {
        return userLicenseRepository.findByUserTableUserId(userId);
    }

    @Override
    public IsLicenseExpiredDTO isLicenseExpired(LicenseRequestParam licenseRequestParam) {
        long userId = licenseRequestParam.getUserId();
        long appId = licenseRequestParam.getAppId();
        Optional<UserLicense> userLicenseAllOptional = userLicenseRepository.findByUserTableUserIdAndApplicationAppId(userId, appId);

        if (userLicenseAllOptional.isEmpty()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID_APP_ID);
        }

        UserLicense userLicense = userLicenseAllOptional.get();

        // Convert the Timestamp to LocalDate
        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = userLicense.getLicenseKey().getExpirationDate().toLocalDateTime().toLocalDate();

        if (currentDate.isAfter(expiryDate)) {
            ErrorHandlerUtil.handleError(ErrorCode.LICENSE_KEY_EXPIRED);
        }
        return getIsLicenseExpiredDTO(userLicense);
    }

    private IsLicenseExpiredDTO getIsLicenseExpiredDTO(UserLicense userLicense) {
        IsLicenseExpiredDTO dto = new IsLicenseExpiredDTO();
        dto.setExpirationDate(Timestamp.valueOf(userLicense.getLicenseKey().getExpirationDate().toLocalDateTime()));
        return dto;
    }
}