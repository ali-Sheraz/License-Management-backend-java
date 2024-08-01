package com.avanza.license.service.impl;


import com.avanza.license.entity.SubscriptionPlan;
import com.avanza.license.repositories.ApplicationRepository;
import com.avanza.license.repositories.SubscriptionPlanRepository;
import com.avanza.license.repositories.UserSubscriptionPlanRepository;
import com.avanza.license.repositories.UserTableRepository;
import com.avanza.license.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private UserTableRepository userTableRepository;

    @Autowired
    private UserSubscriptionPlanRepository userSubscriptionPlanRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public SubscriptionPlan saveSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        return subscriptionPlanRepository.save(subscriptionPlan);
    }
    @Override
    public List<SubscriptionPlan> getAllSubscriptionPlans() {

        return subscriptionPlanRepository.findAll();
    }
    @Override
    public List<SubscriptionPlan> getAllSubscriptionPlansByType(String subscriptionType) {
        return subscriptionPlanRepository.findBySubscriptionType(subscriptionType);
    }

}
