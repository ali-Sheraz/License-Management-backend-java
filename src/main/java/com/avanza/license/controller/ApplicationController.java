package com.avanza.license.controller;

import com.avanza.license.Dto.DataTransferDTO;
import com.avanza.license.entity.Application;
import com.avanza.license.entity.UserSession;
import com.avanza.license.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/application/{userId}")
    public ResponseEntity<DataTransferDTO> saveApplication(@RequestBody Application app, @PathVariable Long userId) {
        DataTransferDTO result = applicationService.saveApplication(app, userId);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/application/{appId}")
    public ResponseEntity<Application> updateApplication(@PathVariable Long appId, @RequestBody Application updatedApp) {
        Application result = applicationService.updateApplication(appId, updatedApp);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/application/{appId}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long appId) {
        applicationService.deleteApplication(appId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/application")
    public ResponseEntity<List<Application>> getAllApplication() {
        List<Application> applications = applicationService.getAllApplications();
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }
    @GetMapping("/application/{userId}")
    public ResponseEntity<List<Application>> getAppByUserId(@PathVariable Long userId) {
        List<Application> applications = applicationService.getAppByUserId(userId);
        return new ResponseEntity<>(applications, HttpStatus.OK);

    }

    @GetMapping("/oneapplication/{appId}")
    public ResponseEntity<Application> getAppById(@PathVariable Long appId) {
        Application application = applicationService.getAppById(appId);
        return new ResponseEntity<>(application, HttpStatus.OK);
    }
}
