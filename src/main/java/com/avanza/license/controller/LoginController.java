package com.avanza.license.controller;

import com.avanza.license.Dto.LoginRequest;
import com.avanza.license.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginRequest> login(@RequestBody LoginRequest login) {
        LoginRequest loginResult = loginService.login(login);
        return new ResponseEntity<>(loginResult, HttpStatus.OK);
    }
}
