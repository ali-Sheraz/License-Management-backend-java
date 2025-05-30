package com.avanza.license.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLicenseFloatAbleDTO {
    private Long userLicenseId;
    private Long userId;
    private String username;
    private String email;
    private Long appId;
    private String appName;
    private Long maxUsers;
    private Long licenseId;
    private String keyValue;
    private Timestamp expirationDate;
    private List<ModuleDto> modules;
}
