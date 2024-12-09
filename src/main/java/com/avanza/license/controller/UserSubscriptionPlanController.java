package com.avanza.license.controller;

import com.avanza.license.Dto.DataTransferDTO;
import com.avanza.license.entity.Application;
import com.avanza.license.entity.UserLicense;
import com.avanza.license.entity.UserSubscriptionPlan;
import com.avanza.license.service.UserSubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserSubscriptionPlanController {

    @Autowired
    private UserSubscriptionPlanService userSubscriptionPlanService;

    @PutMapping("/userSubscriptionPlan/{userId}/{appId}/{subscriptionId}/{macAddress}/{biosId}")
    public ResponseEntity<DataTransferDTO> updateUserSubscriptionPlanByUserSubIdAndSubName(@PathVariable Long userId, @PathVariable Long appId, @PathVariable Long subscriptionId,@PathVariable String macAddress,@PathVariable String biosId) {
        DataTransferDTO result = userSubscriptionPlanService.updateUserSubscriptionPlanByUserIdAndAppIdAndSubName(userId, appId, subscriptionId,macAddress,biosId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/userSubscriptionPlan/{userId}/{appId}")
    public ResponseEntity<UserSubscriptionPlan> getUserSubscriptionByUserIdAndSubscriptionId(@PathVariable Long userId, @PathVariable Long appId) {
        UserSubscriptionPlan result = userSubscriptionPlanService.getUserSubscriptionByUserIdAndAppId(userId, appId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/userSubscriptionPlan")
    public ResponseEntity<List<UserSubscriptionPlan>> getAllUserSubscriptionPlan() {
        List<UserSubscriptionPlan> result = userSubscriptionPlanService.getAllUserSubscriptionPlans();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/userSubscriptionPlan/{userId}")
    public ResponseEntity<List<UserSubscriptionPlan>> getAllUserSubscriptionPlansByUserId(@PathVariable Long userId) {
        List<UserSubscriptionPlan> result = userSubscriptionPlanService.getAllUserSubscriptionPlanByUserId(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
