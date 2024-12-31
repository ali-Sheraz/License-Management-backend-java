package com.avanza.license.service.impl;

import com.avanza.license.service.RegisterPlanDynamicFieldsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RegisterPlanDynamicFieldsServiceImpl implements RegisterPlanDynamicFieldsService {
    @Value("${file.path}")
    private String filePath;

    @Override
    public List<Map<String, String>> getDynamicFieldsData() {
        try {
            File file = ResourceUtils.getFile("classpath:" + filePath);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(file, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error reading JSON file: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, String>> getDynamicFieldsDataByKey(String key) {
        try {
            if (key == null) {
                throw new IllegalArgumentException("The 'key' parameter cannot be null");
            }
            File file = ResourceUtils.getFile("classpath:" + filePath);
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> data = objectMapper.readValue(file, new TypeReference<>() {});

            return data.stream()
                    .filter(item -> key.equals(item.get("key")))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error reading JSON file: " + e.getMessage(), e);
        }
    }
}
