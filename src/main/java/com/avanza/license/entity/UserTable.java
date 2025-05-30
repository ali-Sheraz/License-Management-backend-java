package com.avanza.license.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "USER_TABLES")
@AllArgsConstructor
@NoArgsConstructor
public class UserTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "ROLE") // Add role field
    private String role;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
}
