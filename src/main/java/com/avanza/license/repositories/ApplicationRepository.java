package com.avanza.license.repositories;

import com.avanza.license.entity.Application;
import com.avanza.license.entity.UserSession;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;



public interface ApplicationRepository extends CrudRepository<Application, Long> {
    Optional<Application> findByAppId(Long userId);

    void deleteByAppId(Long appId);
    List<Application> findAll();

    List<Application> findByOwnerUserId(Long userId);

    Boolean existsByAppId(Long appId);
}
