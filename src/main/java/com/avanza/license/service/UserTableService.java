package com.avanza.license.service;

import com.avanza.license.entity.UserTable;

import java.util.List;

public interface UserTableService {
    UserTable saveUser(UserTable user);
    List<UserTable> getAllUsers(); // Change return type to List
    UserTable updateUser(Long userId, UserTable user);
    void deleteUser(Long userId);
    long userCount();
    UserTable getUserById(Long userId);
}
