package com.avanza.license.controller;

import java.util.List;

import com.avanza.license.Dto.DataTransferDTO;
import com.avanza.license.Dto.UserModuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.avanza.license.entity.UserModule;
import com.avanza.license.service.UserModuleService;

@RestController
@RequestMapping("/v1")
public class UserModuleController {

    @Autowired
    private UserModuleService userModuleService;

    @GetMapping("/usermodule")
    public ResponseEntity<List<UserModule>> getAllUserModules() {
        List<UserModule> userModule = userModuleService.getAllUserModule();
        return new ResponseEntity<>(userModule, HttpStatus.OK);
    }
//    @PutMapping("/usermodule/{userId}/{appId}/{moduleId}")
//    public ResponseEntity<UserModuleDTO> updateUserModules(@PathVariable Long userId, @PathVariable Long appId, @PathVariable Long moduleId) {
//        UserModuleDTO result = userModuleService.updateUserModules(userId, appId, moduleId);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
    @GetMapping("/usermodule/{userId}")
    public ResponseEntity<List<UserModule>> getAllUserModulesByUserId(@PathVariable Long userId) {
        List<UserModule> userModule = userModuleService.getAllUserModuleByUserId(userId);
        return new ResponseEntity<>(userModule, HttpStatus.OK);
    }
}