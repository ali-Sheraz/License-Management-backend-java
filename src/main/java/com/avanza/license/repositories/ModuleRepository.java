package com.avanza.license.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avanza.license.entity.Module;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    List<Module> findAll();
    List<Module> findByApplicationName(String applicationName);
    Optional<Module> findByModuleId(Long moduleId);
    Optional<Module> findByIdentificationKey(String identificationKey);

}