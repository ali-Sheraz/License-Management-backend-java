package com.avanza.license.util;

import com.avanza.license.util.CustomApplicationException;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomApplicationException.class)
    public ResponseEntity<Object> handleCustomApplicationException(CustomApplicationException ex) {
        HttpServletRequest request = (HttpServletRequest) RequestContextHolder.currentRequestAttributes()
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        // Build response body
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", ex.getstatusCode());
        responseBody.put("error", HttpStatus.valueOf(ex.getstatusCode()).getReasonPhrase());
        responseBody.put("message", ex.getErrorMessage());
        responseBody.put("path", request.getRequestURI());

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(responseBody, headers, HttpStatus.valueOf(ex.getstatusCode()));
    }


    //@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(CustomApplicationException.class)
//    public ResponseEntity<Object> handleCustomApplicationException(CustomApplicationException ex) {
//        Map<String, Object> responseBody = new HashMap<>();
//        responseBody.put("timestamp", LocalDateTime.now());
//        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
//        responseBody.put("error", "Bad Request");
//        responseBody.put("message", ex.getErrorMessage());  // Custom error message
//        responseBody.put("path", "/license-server/v1/userLicenseKey"); // Adjust path dynamically if needed
//
//        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
//    }
//
//    // Other exception handlers if needed
//}
}

