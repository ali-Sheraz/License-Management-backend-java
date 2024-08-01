package com.avanza.license.repositories;

import com.avanza.license.entity.LicenseKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LicenseKeyRepository extends CrudRepository<LicenseKey, Long> {

    List<LicenseKey> findAll();
    Optional<LicenseKey> findByApplicationAppId(Long appId);
}
