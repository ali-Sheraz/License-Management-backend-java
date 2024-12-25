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
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/v2/application")
    public ResponseEntity<DataTransferDTO> saveApplication(@RequestBody Application app) {
        DataTransferDTO result = applicationService.saveApplication(app);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/v2/application/{appId}")
    public ResponseEntity<Application> updateApplication(@PathVariable Long appId, @RequestBody Application updatedApp) {
        Application result = applicationService.updateApplication(appId, updatedApp);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/v1/application/{appId}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long appId) {
        applicationService.deleteApplication(appId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/v1/application")
    public ResponseEntity<List<Application>> getAllApplication() {
        List<Application> applications = applicationService.getAllApplications();
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }
    @GetMapping("/v1/application/{userId}")
    public ResponseEntity<List<Application>> getAppByUserId(@PathVariable Long userId) {
        List<Application> applications = applicationService.getAppByUserId(userId);
        return new ResponseEntity<>(applications, HttpStatus.OK);

    }

    @GetMapping("/v1/oneapplication/{appId}")
    public ResponseEntity<Application> getAppById(@PathVariable Long appId) {
        Application application = applicationService.getAppById(appId);
        return new ResponseEntity<>(application, HttpStatus.OK);
    }
    @GetMapping("/v1/search")
    public ResponseEntity<List<Application>> searchApplicationsByUsername(@RequestParam String username) {
        List<Application> applications = applicationService.findApplicationsByUsername(username);
        return ResponseEntity.ok(applications);
    }
    @GetMapping("/v1/applicationcount")
    public ResponseEntity<Long> applicationCount() {
        long count = applicationService.applicationCount();
        return ResponseEntity.ok(count); // Return a ResponseEntity with the count and HTTP status 200
    }
}
