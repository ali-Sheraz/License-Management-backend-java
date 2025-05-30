package com.avanza.license.config;

import com.avanza.license.service.impl.CustomUserDetailService;
import com.avanza.license.util.jwt.CustomAccessDeniedHandler;
import com.avanza.license.util.jwt.JwtAuthenticationEntryPoint;
import com.avanza.license.util.jwt.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/v1/authenticate/**", "/v1/register/**", "/h2/**","/v3/**").permitAll() // Publicly accessible endpoints
                .antMatchers("/v1/**").hasAnyRole("USER", "ADMIN") // Both USER and ADMIN can access /v1/**
                .antMatchers("/v2/**").hasRole("ADMIN") // Only ADMIN can access /v2/**
                .anyRequest().authenticated() // All other endpoints require authentication
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Handle unauthorized access
                .accessDeniedHandler(new CustomAccessDeniedHandler()) // Handle forbidden access
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless authentication
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        http.headers().frameOptions().disable(); // Allow H2 console access
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder,
                                                       CustomUserDetailService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }
}
