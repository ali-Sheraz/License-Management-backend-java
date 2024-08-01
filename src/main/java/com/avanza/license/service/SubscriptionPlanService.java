package com.avanza.license.service;

import com.avanza.license.entity.SubscriptionPlan;

import java.util.List;

public interface SubscriptionPlanService {
    SubscriptionPlan saveSubscriptionPlan(SubscriptionPlan subscriptionPlan);
    List<SubscriptionPlan> getAllSubscriptionPlans();

    List<SubscriptionPlan> getAllSubscriptionPlansByType(String subscriptionType);

}
