package com.avanza.license.util;

import com.avanza.license.Dto.CertificateDetails;
import com.avanza.license.Dto.UserLicenseFloatAbleDTO;
import com.avanza.license.entity.SubscriptionPlan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/v1")
public class TestController {
    @RequestMapping("/test")
    public String test() {
        return "License Management Module is  running successfully!";
    }

    @PostMapping("/uploadCertificateDetail")
    public ResponseEntity<String> uploadCerCertificate(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please upload a file!");
        }

        try {
            // Read the file content
            byte[] fileContent = file.getBytes();
            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);

            // Load the certificate
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(is);

            // Check if the certificate is expired
            Date now = new Date();
            if (now.after(x509Cert.getNotAfter())) {
                throw new Exception("Certificate has expired. Please upload a valid certificate.");
            }

            // Prepare certificate details
            CertificateDetails certificateDetails = new CertificateDetails(
                    x509Cert.getSubjectDN().getName(),
                    x509Cert.getIssuerDN().getName(),
                    x509Cert.getSerialNumber().toString(),
                    x509Cert.getNotBefore(),
                    x509Cert.getNotAfter(),
                    x509Cert.getSigAlgName(),
                    x509Cert.getVersion(),
                    x509Cert.getPublicKey().toString(),
                    null
            );

            // Encode the certificate in PEM format
            String pemCert = "-----BEGIN CERTIFICATE-----\n"
                    + Base64.getEncoder().encodeToString(x509Cert.getEncoded())
                    + "\n-----END CERTIFICATE-----";
            certificateDetails.setPemEncodedCertificate(pemCert);
            List<String> moduleNames = ErrorHandlerUtil.getModulesList(certificateDetails.getSubject());

            return new ResponseEntity<>("Certificate uploaded successfully! Details: \n" + moduleNames, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}