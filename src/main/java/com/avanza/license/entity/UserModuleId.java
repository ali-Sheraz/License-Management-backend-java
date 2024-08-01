package com.avanza.license.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModuleId implements Serializable {

    private Long userTable;
    private Long module;
    private Long application;

}