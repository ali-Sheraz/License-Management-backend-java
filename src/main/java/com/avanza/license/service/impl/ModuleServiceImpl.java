package com.avanza.license.service.impl;

import com.avanza.license.Enum.ErrorCode;

import com.avanza.license.entity.Application;
import com.avanza.license.entity.Module;

import com.avanza.license.repositories.ModuleRepository;
import com.avanza.license.service.ModuleService;
import com.avanza.license.util.ErrorHandlerUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;


    @Override
    public Module saveModule(Module module) {
        return moduleRepository.save(module);

    }
    @Override
    public Module getModuleById(Long moduleId) {
        Optional<Module> moduleOptional = moduleRepository.findByModuleId(moduleId);
        if (!moduleOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_MODULE_ID);
        }
        return moduleOptional.get();
    }
    @Override
    public List<Module> getAllModule() {
        return moduleRepository.findAll();
    }
    @Override
    public List<Module> getAllModuleByApplicationName(String applicationName) {
        return moduleRepository.findByApplicationName(applicationName);
    }

    @Override
    public Module updatedModule(Long moduleId, Module updateModule) {
        Optional<Module> existingModuleOptional = moduleRepository.findByModuleId(moduleId);
        if (!existingModuleOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_MODULE_ID);
        }
        Module existingModule = existingModuleOptional.get();
        existingModule.setApplicationName(updateModule.getApplicationName());
        existingModule.setDescription(updateModule.getDescription());
        existingModule.setName(updateModule.getName());
        existingModule.setIdentificationKey(updateModule.getIdentificationKey());
        return moduleRepository.save(existingModule);
    }

}