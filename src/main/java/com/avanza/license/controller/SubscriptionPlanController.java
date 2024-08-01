package com.avanza.license.controller;

import com.avanza.license.entity.SubscriptionPlan;
import com.avanza.license.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class SubscriptionPlanController {

    @Autowired
    private SubscriptionPlanService subscriptionPlanService;

    @PostMapping("/subscriptionPlan")
    public ResponseEntity<SubscriptionPlan> saveSubscriptionPlan(@RequestBody SubscriptionPlan subscriptionPlan) {
        SubscriptionPlan savedSubscriptionPlan = subscriptionPlanService.saveSubscriptionPlan(subscriptionPlan);
        return new ResponseEntity<>(savedSubscriptionPlan,HttpStatus.CREATED);
    }

    @GetMapping("/subscriptionPlan")
    public ResponseEntity<List<SubscriptionPlan>> getAllSubscriptionPlan() {
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanService.getAllSubscriptionPlans();
        return new ResponseEntity<>(subscriptionPlans, HttpStatus.OK);
    }
    @GetMapping("/subscriptionPlan/{subscriptionType}")
    public ResponseEntity<List<SubscriptionPlan>> getAllSubscriptionPlanByType(@PathVariable String subscriptionType ) {
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanService.getAllSubscriptionPlansByType(subscriptionType);
        return new ResponseEntity<>(subscriptionPlans, HttpStatus.OK);
    }
}
