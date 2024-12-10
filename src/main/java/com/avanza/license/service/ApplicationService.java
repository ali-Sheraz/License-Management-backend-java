package com.avanza.license.service;

import com.avanza.license.Dto.DataTransferDTO;
import com.avanza.license.entity.*;

import java.util.List;

public interface ApplicationService {

    DataTransferDTO saveApplication(Application app);

    List<Application> getAllApplications();

    Application updateApplication(Long appId, Application updatedApp);

    void deleteApplication(Long appId);

    Application getAppById(Long appId);

    List<Application> getAppByUserId(Long userId);

}
