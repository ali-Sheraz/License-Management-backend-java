package com.avanza.license.controller;

import java.util.List;

import com.avanza.license.entity.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.avanza.license.entity.Module;
import com.avanza.license.service.ModuleService;

@RestController
@RequestMapping("/v1")
public class ModuleController {
    @Autowired
    private ModuleService moduleService;

    @PostMapping("/module")
    public ResponseEntity<Module> saveModules(@RequestBody Module module) {
        Module result = moduleService.saveModule(module);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long moduleId) {
        Module module = moduleService.getModuleById(moduleId);
        return new ResponseEntity<>(module, HttpStatus.OK);
    }
    @GetMapping("/allmodule")
    public ResponseEntity<List<Module>> getAllModules() {
        List<Module> module = moduleService.getAllModule();
        return new ResponseEntity<>(module, HttpStatus.OK);
    }
    @GetMapping("/getmodule/{applicationName}")
    public ResponseEntity<List<Module>> getAllModulesByApplicationName(@PathVariable String applicationName) {
        List<Module> module = moduleService.getAllModuleByApplicationName(applicationName);
        return new ResponseEntity<>(module, HttpStatus.OK);
    }
    @PutMapping("/module/{moduleId}")
    public ResponseEntity<Module> updateModule(@PathVariable Long moduleId, @RequestBody Module updatedModule) {
        Module module = moduleService.updatedModule(moduleId, updatedModule);
        return new ResponseEntity<>(module, HttpStatus.OK);
    }
}