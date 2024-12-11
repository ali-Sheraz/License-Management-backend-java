package com.avanza.license.service.impl;

import com.avanza.license.Enum.ErrorCode;
import com.avanza.license.entity.AuditLog;
import com.avanza.license.entity.UserTable;
import com.avanza.license.repositories.AuditLogRepository;
import com.avanza.license.repositories.UserTableRepository;
import com.avanza.license.service.UserTableService;
import com.avanza.license.util.ErrorHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserTableServiceImpl implements UserTableService {
@Autowired
private PasswordEncoder passwordEncoder;
    @Autowired
    private UserTableRepository userTableRepository;
    @Autowired
    private AuditLogRepository auditLogRepository;

    @Override
    public UserTable saveUser(UserTable user) {
        String email = user.getEmail();
        String username=user.getUsername();
        Optional<UserTable> userTableOptional = userTableRepository.findByEmail(email);
        if (userTableOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.DUPLICATE_EMAIL);
        }
        // Check for duplicate username
        Optional<UserTable> userTableByUsername = userTableRepository.findByUsername(username);
        if (userTableByUsername.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.DUPLICATE_USERNAME);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Save the user
        UserTable savedUser = userTableRepository.save(user);

        // Log the action in the AuditLog
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("REGISTER");
        auditLog.setEntityName("UserTable");
        auditLog.setEntityId(savedUser.getUserId());
        auditLog.setCreatedOn(new Date());
        auditLog.setCreatedBy(user.getCreatedBy()); // Assuming you set `createdBy` during user registration
        auditLog.setDetails("User registered");

        auditLogRepository.save(auditLog); // Save the audit log entry

        return savedUser;
    }

    @Override
    public List<UserTable> getAllUsers() { // Change return type to List
        return userTableRepository.findAll();
    }

    @Override
    public UserTable getUserById(Long userId) {
        Optional<UserTable> userOptional = userTableRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID);
        }
        return userOptional.get();
    }

    @Override
    public UserTable updateUser(Long userId, UserTable user) {
        Optional<UserTable> existingUserOptional = userTableRepository.findByUserId(userId);
        if (existingUserOptional.isPresent()) {
            UserTable existingUser = existingUserOptional.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setEmail(user.getEmail());
            existingUser.setUpdatedOn(new Date());
            existingUser.setUpdatedBy(user.getUpdatedBy());
            existingUser.setCreatedOn(new Date());
            existingUser.setCreatedBy(user.getUpdatedBy());
            UserTable updatedUser = userTableRepository.save(existingUser);

            // Log the action in the AuditLog
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("UPDATE");
            auditLog.setEntityName("UserTable");
            auditLog.setEntityId(updatedUser.getUserId());
            auditLog.setCreatedOn(new Date()); // When the log is created
            auditLog.setCreatedBy(user.getUpdatedBy()); // Who performed the update
            auditLog.setDetails("User updated");

            auditLogRepository.save(auditLog);

            return updatedUser;
        } else {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID);
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (userTableRepository.existsByUserId(userId)) {
            userTableRepository.deleteByUserId(userId);
        } else {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_USER_ID);
        }
    }


}