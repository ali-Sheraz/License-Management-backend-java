package com.avanza.license.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SessionDTO {
    private String sessionId;
    private Long appId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
