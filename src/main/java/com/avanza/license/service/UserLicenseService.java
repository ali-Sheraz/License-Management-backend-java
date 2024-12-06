package com.avanza.license.service;

import com.avanza.license.Dto.IsLicenseExpiredDTO;
import com.avanza.license.Dto.LicenseRequestParam;
import com.avanza.license.Dto.SessionDTO;
import com.avanza.license.Dto.UserLicenseFloatAbleDTO;
import com.avanza.license.entity.Application;
import com.avanza.license.entity.UserLicense;

import java.util.List;

public interface UserLicenseService {
    List<UserLicense> getAllUserLicenses();

    UserLicense getLicenseByUserIdAndAppId(Long userId, Long appId);

    UserLicenseFloatAbleDTO isLicenseKeyValid(Long userId, Long appId, String loginId, String keyValue);
    UserLicenseFloatAbleDTO isLicenseKeyValidForFloatAbleMatrix(LicenseRequestParam licenseRequestParam);
    SessionDTO insertingSession(Long userId, Long appId, String loginId);

    List<UserLicense> getUserLicenseByUserId(Long userId);

    IsLicenseExpiredDTO isLicenseExpired(LicenseRequestParam licenseRequestParam);

}

