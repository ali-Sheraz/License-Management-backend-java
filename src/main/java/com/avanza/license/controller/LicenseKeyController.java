package com.avanza.license.controller;

import com.avanza.license.entity.LicenseKey;
import com.avanza.license.entity.Module;
import com.avanza.license.service.LicenseKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class LicenseKeyController {

    @Autowired
    private LicenseKeyService licenseKeyService;

    @GetMapping("/licenseKey")
    public ResponseEntity<List<LicenseKey>> getAllLicenseKey() {
        List<LicenseKey> licenseKeys = licenseKeyService.getAllLicenseKeys();
        return new ResponseEntity<>(licenseKeys, HttpStatus.OK);
    }
    @GetMapping("/oneLicenseKey/{appId}")
    public ResponseEntity<LicenseKey> getLicenseKeyByAppId(@PathVariable Long appId) {
        LicenseKey licenseKey = licenseKeyService.getLicenseKeyByAppId(appId);
        return new ResponseEntity<>(licenseKey, HttpStatus.OK);
    }
}
