package com.avanza.license.service.impl;

import com.avanza.license.Enum.ErrorCode;
import com.avanza.license.entity.LicenseKey;
import com.avanza.license.entity.UserTable;
import com.avanza.license.repositories.LicenseKeyRepository;
import com.avanza.license.service.LicenseKeyService;
import com.avanza.license.util.ErrorHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class LicenseKeyServiceImpl implements LicenseKeyService {

    @Autowired
    private LicenseKeyRepository licenseKeyRepository;

    @Override
    public List<LicenseKey> getAllLicenseKeys() {
        return licenseKeyRepository.findAll();
    }
    @Override
    public LicenseKey getLicenseKeyByAppId(Long appId) {
        Optional<LicenseKey> licenseKeyOptional = licenseKeyRepository.findByApplicationAppId(appId);
        if (!licenseKeyOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_APP_ID);
        }
        return licenseKeyOptional.get();
    }
}
