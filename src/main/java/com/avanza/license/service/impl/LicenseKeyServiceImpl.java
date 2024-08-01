package com.avanza.license.service.impl;

import com.avanza.license.entity.LicenseKey;
import com.avanza.license.repositories.LicenseKeyRepository;
import com.avanza.license.service.LicenseKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LicenseKeyServiceImpl implements LicenseKeyService {

    @Autowired
    private LicenseKeyRepository licenseKeyRepository;

    @Override
    public List<LicenseKey> getAllLicenseKeys() {
        return licenseKeyRepository.findAll();
    }
}
