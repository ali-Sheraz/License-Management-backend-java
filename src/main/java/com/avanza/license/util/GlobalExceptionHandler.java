package com.avanza.license.util;

import com.avanza.license.util.CustomApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomApplicationException.class)
    public ResponseEntity<Object> handleCustomApplicationException(CustomApplicationException ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("error", "Bad Request");
        responseBody.put("message", ex.getErrorMessage());  // Custom error message
        responseBody.put("path", "/license-server/v1/userLicenseKey"); // Adjust path dynamically if needed

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    // Other exception handlers if needed
}
