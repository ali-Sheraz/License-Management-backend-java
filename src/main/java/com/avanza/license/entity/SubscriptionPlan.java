package com.avanza.license.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "SUBSCRIPTION_PLANS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SUBSCRIPTION_ID")
    private long subscriptionId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "PRICE")
    private double price;

    @Column(name = "DURATION_MONTHS", nullable = false)
    private Long durationMonths;

    @Column(name = "SUBSCRIPTION_TYPE", nullable = false)
    private String subscriptionType;

    @Column(name = "START_DATE")
    private Timestamp startDate;

    @Column(name = "END_DATE")
    private Timestamp endDate;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
}
