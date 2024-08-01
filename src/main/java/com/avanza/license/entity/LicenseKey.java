package com.avanza.license.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "LICENSE_KEYS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseKey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LICENSE_ID")
    private Long licenseId;

    @Column(name = "KEY_VALUE", nullable = false)
    private String keyValue;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "EXPIRATION_DATE")
    private java.sql.Timestamp expirationDate;


    @ManyToOne
    @JoinColumn(name = "APP_ID", nullable = false)
    private Application application;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "UPDATED_BY")
    private String updatedBy;


}
