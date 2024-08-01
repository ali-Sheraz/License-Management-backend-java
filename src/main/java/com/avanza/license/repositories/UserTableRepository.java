package com.avanza.license.repositories;

import com.avanza.license.entity.UserTable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserTableRepository extends CrudRepository<UserTable, Long> {
    Optional<UserTable> findByUserId(Long userId);
    Boolean existsByUserId(Long userId);
    Optional<UserTable> findByEmail(String email);
    List<UserTable> findAll();
    void deleteByUserId(Long userId);
}

