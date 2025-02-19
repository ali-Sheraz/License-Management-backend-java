package com.avanza.license.service;

public interface GenerateCertificateService {
    String generateKeys(
            String passphrase,
            String appId,
            String userId,
            String moduleId,
            String maxUser,
            String licenseKey,
            int validityDays
    );
}
