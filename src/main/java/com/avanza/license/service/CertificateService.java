package com.avanza.license.service;

import com.avanza.license.entity.SubscriptionPlan;
import org.springframework.web.multipart.MultipartFile;

public interface CertificateService {
    
    SubscriptionPlan uploadCerCertificate(MultipartFile file) throws Exception;

    SubscriptionPlan updateCerCertificate(MultipartFile file,long subscriptionId) throws Exception;
    
}
