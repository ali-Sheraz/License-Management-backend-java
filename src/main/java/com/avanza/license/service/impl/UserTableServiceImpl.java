package com.avanza.license.service.impl;

import com.avanza.license.Enum.ErrorCode;
import com.avanza.license.entity.UserTable;
import com.avanza.license.repositories.UserTableRepository;
import com.avanza.license.service.UserTableService;
import com.avanza.license.util.ErrorHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserTableServiceImpl implements UserTableService {

    @Autowired
    private UserTableRepository userTableRepository;

    @Override
    public UserTable saveUser(UserTable user) {
        String email= user.getEmail();
        Optional<UserTable> userTableOptional = userTableRepository.findByEmail(email);
        if (userTableOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.DUPLICATE_EMAIL);
        }
        return userTableRepository.save(user);
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
            return userTableRepository.save(existingUser);
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