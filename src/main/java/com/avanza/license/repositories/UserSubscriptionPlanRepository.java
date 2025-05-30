package com.avanza.license.repositories;

import com.avanza.license.entity.Application;
import com.avanza.license.entity.UserSubscriptionPlan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface UserSubscriptionPlanRepository extends CrudRepository<UserSubscriptionPlan, Long> {
    Optional<UserSubscriptionPlan> findByUserTableUserIdAndApplicationAppId(Long userId, Long appId);
    List<UserSubscriptionPlan> findAll();

    List<UserSubscriptionPlan> findByUserTableUserId(Long userId);

}
