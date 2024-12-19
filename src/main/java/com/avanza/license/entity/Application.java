package com.avanza.license.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "APPLICATIONS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "APP_ID")
    private Long appId;

    @Column(name = "APP_NAME", nullable = false)
    private String appName;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserTable owner;

    @ManyToOne
    @JoinColumn(name = "SUBSCRIPTION_ID", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(
            name = "APPLICATION_MODULES",
            joinColumns = @JoinColumn(name = "APP_ID"),
            inverseJoinColumns = @JoinColumn(name = "MODULE_ID")
    )
    private List<Module> modules;

    @Column(name = "MAX_USERS", nullable = false)
    private int maxUsers = 1;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name = "FQDN")
    private String fqdn;

    @Column(name = "BIOS_ID")
    private String biosId;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "UPDATED_BY")
    private String updatedBy;


    @Column(name = "EXPIRATION_DATE")
    private java.sql.Timestamp expirationDate;

// @Transient
    // private CertificateDetails certificateDetails;
    // @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonManagedReference
    // private List<Module> modules;
}