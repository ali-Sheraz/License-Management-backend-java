package com.avanza.license.service;

import java.util.List;

import com.avanza.license.Dto.DataTransferDTO;
import com.avanza.license.Dto.UserModuleDTO;
import com.avanza.license.entity.*;

public interface UserModuleService {
    List<UserModule> getAllUserModule();

    List<UserModule> getAllUserModuleByUserId(Long userId);
//    UserModuleDTO updateUserModules(Long userId, Long appId, Long moduleId);
}