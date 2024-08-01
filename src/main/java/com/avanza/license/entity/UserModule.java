package com.avanza.license.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "USER_MODULES")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserModuleId.class)
public class UserModule {

    @Id
    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private UserTable userTable;

    @Id
    @ManyToOne
    @JoinColumn(name = "APP_ID", insertable = false, updatable = false)
    private Application application;

    @Id
    @ManyToOne
    @JoinColumn(name = "MODULE_ID", insertable = false, updatable = false)
    private Module module;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
}