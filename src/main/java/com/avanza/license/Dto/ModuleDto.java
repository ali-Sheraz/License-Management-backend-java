package com.avanza.license.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDto {
    private Long moduleId;
    private String applicationName;
    private String name;
    private String identificationKey;
}
