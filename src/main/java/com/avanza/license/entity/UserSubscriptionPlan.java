package com.avanza.license.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "USER_SUBSCRIPTION_PLANS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_SUBSCRIPTION_ID")
    private Long userSubscriptionId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserTable userTable;

    @ManyToOne
    @JoinColumn(name = "APP_ID", nullable = false)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "SUBSCRIPTION_ID", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date", nullable = false)
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
