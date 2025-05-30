package com.avanza.license.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTransferDTO {
    private Long userId;
    private String username;
    private String email;
    private Long appId;
    private String appName;
    private String description;
    private Long licenseId;
    private String keyValue;
    private List<ModuleDto> modules;
}
