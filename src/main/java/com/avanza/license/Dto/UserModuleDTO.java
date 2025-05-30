package com.avanza.license.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModuleDTO {
    private Long userId;
    private String username;
    private String email;
    private Long appId;
    private String appName;
    private Long moduleId;
    private String applicationName;
    private String name;
    private String identificationKey;
}
