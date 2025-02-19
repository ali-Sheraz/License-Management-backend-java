package com.avanza.license.controller;

import com.avanza.license.service.GenerateCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class GenerateCertificateController {
    @Autowired
    private GenerateCertificateService generateCertificateService;

    @PostMapping("/generate")
    public String generateKeys(
            @RequestParam String passphrase,
            @RequestParam String appId,
            @RequestParam String userId,
            @RequestParam String moduleId,
            @RequestParam String maxUser,
            @RequestParam String licenseKey,
            @RequestParam int validityDays
    ) {
        return generateCertificateService.generateKeys(passphrase, appId, userId, moduleId, maxUser, licenseKey, validityDays);
    }
}
