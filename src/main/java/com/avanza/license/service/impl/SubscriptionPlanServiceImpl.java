package com.avanza.license.service.impl;


import com.avanza.license.entity.AuditLog;
import com.avanza.license.entity.SubscriptionPlan;
import com.avanza.license.repositories.*;
import com.avanza.license.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Autowired
    private AuditLogRepository auditLogRepository;


    @Override
    public SubscriptionPlan saveSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        SubscriptionPlan subplan= subscriptionPlanRepository.save(subscriptionPlan);
        // Log the action in the AuditLog
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("REGISTER");
        auditLog.setEntityName("SubscriptionPlan");
        auditLog.setEntityId(subplan.getSubscriptionId());
        auditLog.setCreatedOn(new Date());
        auditLog.setCreatedBy(subplan.getCreatedBy()); // Assuming you set `createdBy` during user registration
        auditLog.setDetails("SubscriptionPlan Registered");
        auditLogRepository.save(auditLog);
        return subplan;
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
