package com.avanza.license.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "USER_LICENSES")
@NoArgsConstructor
@AllArgsConstructor
public class UserLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_LICENSE_ID")
    private Long userLicenseId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserTable userTable ;

    @ManyToOne
    @JoinColumn(name = "APP_ID", nullable = false)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "LICENSE_ID", nullable = false)
    private LicenseKey licenseKey;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    // Getters and setters
}
