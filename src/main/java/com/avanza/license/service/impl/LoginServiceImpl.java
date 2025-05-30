package com.avanza.license.service.impl;

import com.avanza.license.Dto.LoginRequest;
import com.avanza.license.Enum.ErrorCode;
import com.avanza.license.entity.UserTable;
import com.avanza.license.repositories.UserTableRepository;
import com.avanza.license.service.LoginService;
import com.avanza.license.util.ErrorHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserTableRepository userTableRepository;

    @Override
    public LoginRequest login(LoginRequest login) {
        String email = login.getEmail();
        Optional<UserTable> userTableOptional = userTableRepository.findByEmail(email);

        if (!userTableOptional.isPresent()) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_CREDENTIALS);
        }
        UserTable userTable = userTableOptional.get();

        if (!login.getPassword().equals(userTable.getPassword())) {
            ErrorHandlerUtil.handleError(ErrorCode.INVALID_PASSWORD);
        }
        return login;
    }
}
