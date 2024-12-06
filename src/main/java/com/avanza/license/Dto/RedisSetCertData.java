package com.avanza.license.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisSetCertData {
    private String userId;
    private String appId;
    private int maxUsers;
    private String keyValue;
//    private Timestamp expirationDate;
    private List<ModuleDto> modules;
}
