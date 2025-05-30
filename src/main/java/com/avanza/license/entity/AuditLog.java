package com.avanza.license.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AUDIT_LOG")
@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACTION", nullable = false)
    private String action;

    @Column(name = "ENTITY_NAME", nullable = false)
    private String entityName;

    @Column(name = "ENTITY_ID", nullable = false)
    private Long entityId;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DETAILS", columnDefinition = "TEXT")
    private String details;

}
