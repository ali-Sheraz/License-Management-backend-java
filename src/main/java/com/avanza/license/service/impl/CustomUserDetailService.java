package com.avanza.license.service.impl;

import com.avanza.license.entity.UserTable;

import com.avanza.license.repositories.UserTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserTableRepository userTableRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserTable user = userTableRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("Password from DB: " + user.getPassword());
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
        return new User(user.getUsername(), user.getPassword(), Collections.singleton(authority));
    }
}
