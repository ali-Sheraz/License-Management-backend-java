package com.avanza.license.controller;

import com.avanza.license.Dto.IsLicenseExpiredDTO;
import com.avanza.license.Dto.LicenseRequestParam;
import com.avanza.license.Dto.SessionDTO;
import com.avanza.license.Dto.UserLicenseFloatAbleDTO;
import com.avanza.license.entity.UserLicense;
import com.avanza.license.service.UserLicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserLicenseController {

    @Autowired
    private UserLicenseService userLicenseService;

    @GetMapping("/userLicense/{userId}/{appId}")
    public ResponseEntity<UserLicense> getUserLicenseByUserIdAndAppId(@PathVariable Long userId, @PathVariable Long appId) {
        UserLicense userLicense = userLicenseService.getLicenseByUserIdAndAppId(userId, appId);
        return new ResponseEntity<>(userLicense, HttpStatus.OK);

    }

    @GetMapping("/userLicense")
    public ResponseEntity<List<UserLicense>> getAllUserLicense() {
        List<UserLicense> userLicenses = userLicenseService.getAllUserLicenses();
        return new ResponseEntity<>(userLicenses, HttpStatus.OK);
    }
    @GetMapping("/userLicenseKey")
    public ResponseEntity<UserLicenseFloatAbleDTO> isLicenseKeyValid(@RequestParam("userId") Long userId, @RequestParam("appId") Long appId, @RequestParam("loginId") String loginId, @RequestParam("keyValue") String keyValue) {
        UserLicenseFloatAbleDTO UserLicense = userLicenseService.isLicenseKeyValid(userId, appId,loginId, keyValue);
        return new ResponseEntity<>(UserLicense, HttpStatus.OK);
    }

    @PostMapping("/userLicenseFloatAbleMatrix")
    public ResponseEntity<UserLicenseFloatAbleDTO> isLicenseKeyValidForFloatAbleMatrix(@RequestBody LicenseRequestParam licenseRequestParam) {
        UserLicenseFloatAbleDTO UserLicense = userLicenseService.isLicenseKeyValidForFloatAbleMatrix(licenseRequestParam);
        return new ResponseEntity<>(UserLicense, HttpStatus.OK);
    }
    @PostMapping("/isLicenseExpiredUser")
    public ResponseEntity<IsLicenseExpiredDTO> isLicenseExpired(@RequestBody LicenseRequestParam licenseRequestParam) {
        IsLicenseExpiredDTO UserLicense = userLicenseService.isLicenseExpired(licenseRequestParam);
        return new ResponseEntity<>(UserLicense, HttpStatus.OK);
    }

    @GetMapping("/insertingSession/{userId}/{appId}/{loginId}")
    public ResponseEntity<SessionDTO> insertingSession(@PathVariable Long userId, @PathVariable Long appId, @PathVariable String loginId) {
        SessionDTO userSession = userLicenseService.insertingSession(userId, appId, loginId);
        return new ResponseEntity<>(userSession, HttpStatus.OK);
    }
    @GetMapping("/userLicense/{userId}")
    public ResponseEntity<List<UserLicense>> getUserLicenseByUserId(@PathVariable Long userId) {
        List<UserLicense> userLicenses = userLicenseService.getUserLicenseByUserId(userId);
        return new ResponseEntity<>(userLicenses, HttpStatus.OK);

    }

}
