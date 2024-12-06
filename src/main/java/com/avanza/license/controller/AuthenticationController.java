package com.avanza.license.controller;

import com.avanza.license.entity.UserTable;
import com.avanza.license.service.UserTableService;
import com.avanza.license.service.impl.CustomUserDetailService;
import com.avanza.license.util.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService userDetailsService;

    @Autowired
    private UserTableService userTableService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public Map<String, String> authenticate(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String jwt = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities().toString());

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("role", userDetails.getAuthorities().toString());
        return response;
    }
    @PostMapping("/register")
    public ResponseEntity<UserTable> saveUser(@RequestBody UserTable user) {
        UserTable savedUser = userTableService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }
}
