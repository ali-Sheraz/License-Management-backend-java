package com.avanza.license.service;

import java.util.List;

import com.avanza.license.entity.Module;


public interface ModuleService {
    Module saveModule(Module module);
    Module getModuleById(Long moduleId);

    List<Module> getAllModule();
    List<Module> getAllModuleByApplicationName(String applicationName);

    Module updatedModule(Long moduleId,Module updateModule);
}