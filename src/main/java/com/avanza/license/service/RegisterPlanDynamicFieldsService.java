package com.avanza.license.service;

import java.util.List;
import java.util.Map;

public interface RegisterPlanDynamicFieldsService {
    List<Map<String, String>> getDynamicFieldsData();
    List<Map<String, String>> getDynamicFieldsDataByKey(String key);
}
