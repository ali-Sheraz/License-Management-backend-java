package com.avanza.license.repositories;

import com.avanza.license.entity.SubscriptionPlan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface SubscriptionPlanRepository extends CrudRepository<SubscriptionPlan, Long> {
    Optional<SubscriptionPlan> findBySubscriptionId(Long subscriptionId);
    List<SubscriptionPlan> findAll();
    List<SubscriptionPlan> findBySubscriptionType(String subscriptionType);

}
