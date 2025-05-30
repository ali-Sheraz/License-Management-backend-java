package com.avanza.license.repositories;


import com.avanza.license.entity.Application;
import com.avanza.license.entity.UserLicense;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserLicenseRepository extends CrudRepository<UserLicense, Long> {
    Optional<UserLicense> findByUserTableUserIdAndApplicationAppId(Long userId, Long appId);
    List<UserLicense> findAll();
    Optional<UserLicense> findByUserTableUserIdAndApplicationAppIdAndLicenseKeyKeyValue(Long userId, Long appId,String keyValue);
    List<UserLicense> findByUserTableUserId(Long userId);
}
