package com.avanza.license.service;

import com.avanza.license.Dto.DataTransferDTO;
import com.avanza.license.entity.Application;
import com.avanza.license.entity.UserLicense;
import com.avanza.license.entity.UserSubscriptionPlan;

import java.util.List;

public interface UserSubscriptionPlanService {
    List<UserSubscriptionPlan> getAllUserSubscriptionPlans();

    List<UserSubscriptionPlan> getAllUserSubscriptionPlanByUserId(Long userId);

    UserSubscriptionPlan getUserSubscriptionByUserIdAndAppId(Long userId, Long appId);

    DataTransferDTO updateUserSubscriptionPlanByUserIdAndAppIdAndSubName(Long userId, Long appId, Long subscriptionId,String fqdn,String biosId);
//UserLicense updateUserSubscriptionPlanByUserIdAndAppIdAndSubName(Long userId, Long appId, Long subscriptionId);
}
