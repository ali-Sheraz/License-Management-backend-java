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
                .antMatchers("/v1/authenticate", "/h2/**","/v1/generate-client-key","/v1/certificate-info","/v1/uploadCertificateDetail","/v1/userLicenseFloatAbleMatrix/**","/v1/userLicenseKey/**","/v1/insertingSession/**","/v1/deleteUserSession/**","/v2/**","/v1/isLicenseExpiredUser/**").permitAll()  // Allow authentication endpoint
                .antMatchers("/v1/userTable/**").hasRole("ADMIN")  // Only allow admins to access user table
                .anyRequest().authenticated()  // Any other request requires authentication
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // Handle unauthorized access
                .accessDeniedHandler(new CustomAccessDeniedHandler())  // Handle forbidden access
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Stateless authentication
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter

        http.headers().frameOptions().disable();  // Disable frame options (required for H2 console)
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
