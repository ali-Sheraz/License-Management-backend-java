package com.avanza.license.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.avanza.license.Dto.DataTransferDTO;
import com.avanza.license.Dto.UserModuleDTO;
import com.avanza.license.Enum.ErrorCode;
import com.avanza.license.entity.*;
import com.avanza.license.entity.Module;
import com.avanza.license.repositories.ApplicationRepository;
import com.avanza.license.repositories.ModuleRepository;
import com.avanza.license.util.ErrorHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.avanza.license.repositories.UserModuleRepository;
import com.avanza.license.service.UserModuleService;

@Service
public class UserModuleServiceImpl implements UserModuleService {

    @Autowired
    private UserModuleRepository userModuleRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public List<UserModule> getAllUserModule() {
        return userModuleRepository.findAll();
    }

//    @Override
////    @CachePut(value = "userModule", key = "#userId")
//    public UserModuleDTO updateUserModules(Long userId, Long appId, Long moduleId) {
//        Optional<UserModule> userModuleOptional = userModuleRepository.findByUserTableUserIdAndApplicationAppId(userId, appId);
//        if (!userModuleOptional.isPresent()) {
//            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID_APP_ID);
//        }
//        UserModule userModule = userModuleOptional.get();
//        Optional<Module> moduleOptional = moduleRepository.findByModuleId(moduleId);
//        if (moduleOptional.isPresent()) {
//            Module module = moduleOptional.get();
//
//            // Create a new UserModule instance
//            UserModule newUserModule = new UserModule();
//            newUserModule.setUserTable(userModule.getUserTable());
//            newUserModule.setApplication(userModule.getApplication());
//            newUserModule.setModule(module);
//            newUserModule.setUpdatedOn(new Date());
//            newUserModule.setCreatedOn(new Date());
//            newUserModule.setUpdatedBy("System");
//            newUserModule.setCreatedBy("System");
//
//
//            UserModule savedUserModule = userModuleRepository.save(newUserModule);
//
//            // Delete the old UserModule instance
//            userModuleRepository.delete(userModule);
//
//            Optional<Application> applicationOptional = applicationRepository.findByAppId(appId);
//            if (applicationOptional.isPresent()) {
//                Application application = applicationOptional.get();
//                application.setModule(module);
//                applicationRepository.save(application);
//            }
//            return getDataTransferDTO(savedUserModule);
//        } else {
//            ErrorHandlerUtil.handleError(ErrorCode.INVALID_MODULE_ID);
//            return null;
//        }
//    }
//
//    private static UserModuleDTO getDataTransferDTO(UserModule savedUserModule) {
//        UserModuleDTO userModuleDTO = new UserModuleDTO();
//        userModuleDTO.setUserId(savedUserModule.getApplication().getOwner().getUserId());
//        userModuleDTO.setUsername(savedUserModule.getApplication().getOwner().getUsername());
//        userModuleDTO.setEmail(savedUserModule.getApplication().getOwner().getEmail());
//        userModuleDTO.setAppId(savedUserModule.getApplication().getAppId());
//        userModuleDTO.setAppName(savedUserModule.getApplication().getAppName());
//        userModuleDTO.setModuleId(savedUserModule.getModule().getModuleId());
//        userModuleDTO.setApplicationName(savedUserModule.getModule().getApplicationName());
//        userModuleDTO.setName(savedUserModule.getModule().getName());
//        userModuleDTO.setIdentificationKey(savedUserModule.getModule().getIdentificationKey());
//        return userModuleDTO;
//    }

    @Override
//    @Cacheable(value = "userModule", key = "#userId")
    public List<UserModule> getAllUserModuleByUserId(Long userId) {
        System.out.println("come is :"+userId);
        return userModuleRepository.findByUserTableUserId(userId);
    }

}
