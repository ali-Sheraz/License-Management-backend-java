package com.avanza.license.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "USER_SESSIONS")
@IdClass(UserSessionId.class)
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {
    @Id
    @Column(name = "SESSION_ID")
    private String sessionId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserTable userTable;

    @Id
    @ManyToOne
    @JoinColumn(name = "APP_ID", nullable = false)
    private Application application;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

}
