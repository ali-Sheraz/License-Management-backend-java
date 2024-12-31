package com.avanza.license.controller;

import com.avanza.license.service.RegisterPlanDynamicFieldsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class RegisterPlanDynamicFieldsController {

    @Autowired
    private RegisterPlanDynamicFieldsService registerPlanDynamicFieldsService;

    // Endpoint to get all data
    @GetMapping("/dynamicFields")
    public ResponseEntity<List<Map<String, String>>> getAllData() {
        try {
            List<Map<String, String>> data = registerPlanDynamicFieldsService.getDynamicFieldsData();
            return new ResponseEntity<>(data, HttpStatus.OK); // HTTP 200 OK
        } catch (Exception e) {
            // Return an appropriate error message and status
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // HTTP 500
        }
    }

    // Endpoint to get data filtered by key
    @GetMapping("/dynamicFieldsByKey")
    public ResponseEntity<List<Map<String, String>>> getAllDataByKey(@RequestParam(required = false) String key) {
        try {
            List<Map<String, String>> data = registerPlanDynamicFieldsService.getDynamicFieldsDataByKey(key);

            if (data.isEmpty()) {
                // Return HTTP 404 if no data found for the given key
                return new ResponseEntity<>(data, HttpStatus.NOT_FOUND); // HTTP 404 Not Found
            }

            return new ResponseEntity<>(data, HttpStatus.OK); // HTTP 200 OK
        } catch (Exception e) {
            // Return an appropriate error message and status
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // HTTP 500
        }
    }
}
