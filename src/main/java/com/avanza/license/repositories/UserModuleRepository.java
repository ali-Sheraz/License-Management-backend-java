package com.avanza.license.repositories;

import java.util.List;
import java.util.Optional;

import com.avanza.license.entity.UserSubscriptionPlan;
import org.springframework.data.repository.CrudRepository;

import com.avanza.license.entity.UserModule;
import com.avanza.license.entity.UserModuleId;

public interface UserModuleRepository extends CrudRepository<UserModule, UserModuleId> {

    List<UserModule> findAll();

    List<UserModule> findByUserTableUserId(Long userId);
    Optional<UserModule> findByUserTableUserIdAndApplicationAppId(Long userId, Long appId);

}