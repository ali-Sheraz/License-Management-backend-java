package com.avanza.license.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "MODULES")
@Data
@NoArgsConstructor
@AllArgsConstructor
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "moduleId")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MODUlE_ID")
    private Long moduleId;

    // @ManyToOne
    // @JoinColumn(name = "APPLICATION_ID", nullable = false)
    // @JsonBackReference
    // @JsonIgnoreProperties("modules")
    // private Application application;

    @Column(name = "APPLICATION_NAME", nullable = false, length = 255)
    private String applicationName;

    @Column(name = "NAME", nullable = false, length = 255)
    private String name;

    @Column(name = "IDENTIFICATION_KEY", nullable = false, unique = true, length = 255)
    private String identificationKey;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
}