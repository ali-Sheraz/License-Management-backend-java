package com.avanza.license.service;

import com.avanza.license.entity.LicenseKey;

import java.util.List;

public interface LicenseKeyService {
    List<LicenseKey> getAllLicenseKeys();
    LicenseKey getLicenseKeyByAppId(Long appId);
}
