package com.avanza.license.controller;

import com.avanza.license.entity.SubscriptionPlan;
import com.avanza.license.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping("/uploadCertificate")
    public ResponseEntity<String> uploadCerCertificate(@RequestParam("file") MultipartFile file) {
        try {
            SubscriptionPlan certificateDetails = certificateService.uploadCerCertificate(file);
            return new ResponseEntity<>("Certificate uploaded successfully! Details: \n" + certificateDetails, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/updateCertificate/{subscriptionId}")
    public ResponseEntity<String> updateCerCertificate(@RequestParam("file") MultipartFile file,@PathVariable long subscriptionId) {
        try {
            SubscriptionPlan certificateDetails = certificateService.updateCerCertificate(file,subscriptionId);
            return new ResponseEntity<>("Certificate updated successfully! Details: \n" + certificateDetails, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
