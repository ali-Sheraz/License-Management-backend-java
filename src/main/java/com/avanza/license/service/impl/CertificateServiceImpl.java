package com.avanza.license.service.impl;
 
import com.avanza.license.Dto.CertificateDetails;
import com.avanza.license.Enum.ErrorCode;
import com.avanza.license.entity.*;
import com.avanza.license.entity.Module;
import com.avanza.license.repositories.ApplicationRepository;
import com.avanza.license.repositories.ModuleRepository;
import com.avanza.license.repositories.SubscriptionPlanRepository;
import com.avanza.license.repositories.UserModuleRepository;
import com.avanza.license.service.CertificateService;
import com.avanza.license.util.ErrorHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private UserModuleRepository userModuleRepository;

    @Override
    public SubscriptionPlan uploadCerCertificate(MultipartFile file) throws Exception {
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

            return insertSubscriptionPlan(certificateDetails);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to read or process certificate. Error: " + e.getMessage());
        }
    }

    private SubscriptionPlan insertSubscriptionPlan(CertificateDetails certificateDetails) {
        try {
            // Convert Instant to Timestamp directly
            Timestamp startDate = Timestamp.from(certificateDetails.getValidFrom().toInstant());
            Timestamp endDate = Timestamp.from(certificateDetails.getExpiryDate().toInstant());

            // Calculate months between dates
            LocalDate startLocalDate = startDate.toLocalDateTime().toLocalDate();
            LocalDate endLocalDate = endDate.toLocalDateTime().toLocalDate();
            long monthsBetween = ChronoUnit.MONTHS.between(startLocalDate, endLocalDate);

            SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
            subscriptionPlan.setName(ErrorHandlerUtil.getOrganizationName(certificateDetails.getSubject())); // Get organization name
            subscriptionPlan.setDescription(ErrorHandlerUtil.getCommonName(certificateDetails.getSubject())); // Get common name
            subscriptionPlan.setStartDate(startDate);
            subscriptionPlan.setEndDate(endDate);
            subscriptionPlan.setDurationMonths(monthsBetween);
            subscriptionPlan.setSubscriptionType("Custom");
            subscriptionPlan.setCreatedOn(new Date());
            subscriptionPlan.setCreatedBy("system");
            subscriptionPlan.setUpdatedOn(new Date());
            subscriptionPlan.setUpdatedBy("System");

            return subscriptionPlanRepository.save(subscriptionPlan);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //update only subscription plan expiry date
    @Override
    public SubscriptionPlan updateCerCertificate(MultipartFile file,long subscriptionId) throws Exception {
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

//                Check if the certificate is expired
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

            return updateInsertedSubscriptionPlan(certificateDetails,subscriptionId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to read or process certificate. Error: " + e.getMessage());
        }
    }
    private SubscriptionPlan updateInsertedSubscriptionPlan(CertificateDetails certificateDetails,long subscriptionId) {
        try {
            Optional<SubscriptionPlan> sOptional=subscriptionPlanRepository.findBySubscriptionId(subscriptionId);
            if (!sOptional.isPresent()) {
                ErrorHandlerUtil.handleError(ErrorCode.INVALID_SUBSCRIPTION_ID);
            }
            SubscriptionPlan existingSubscriptionPlan=sOptional.get();
            Timestamp startDate = Timestamp.from(certificateDetails.getValidFrom().toInstant());
            Timestamp endDate = Timestamp.from(certificateDetails.getExpiryDate().toInstant());

            // Calculate months between dates
            LocalDate startLocalDate = startDate.toLocalDateTime().toLocalDate();
            LocalDate endLocalDate = endDate.toLocalDateTime().toLocalDate();
            long monthsBetween = ChronoUnit.MONTHS.between(startLocalDate, endLocalDate);
            existingSubscriptionPlan.setName(ErrorHandlerUtil.getOrganizationName(certificateDetails.getSubject())); // Get organization name
            existingSubscriptionPlan.setDescription(ErrorHandlerUtil.getCommonName(certificateDetails.getSubject())); // Get common name
            existingSubscriptionPlan.setStartDate(startDate);
            existingSubscriptionPlan.setEndDate(endDate);
            existingSubscriptionPlan.setDurationMonths(monthsBetween);
            existingSubscriptionPlan.setCreatedOn(new Date());
            existingSubscriptionPlan.setCreatedBy("system");
            existingSubscriptionPlan.setUpdatedOn(new Date());
            existingSubscriptionPlan.setUpdatedBy("System");

            return subscriptionPlanRepository.save(existingSubscriptionPlan);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //update only subscription plan expiry date and ma user also
    @Override
    public SubscriptionPlan updateCerCertificateWithMaxUser(MultipartFile file,long subscriptionId) throws Exception {
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

//                Check if the certificate is expired
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

            return updateInsertedSubscriptionPlanWithApplicationMaxUser(certificateDetails,subscriptionId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to read or process certificate. Error: " + e.getMessage());
        }
    }
    private SubscriptionPlan updateInsertedSubscriptionPlanWithApplicationMaxUser(CertificateDetails certificateDetails,long subscriptionId) {
        try {
            String appId=ErrorHandlerUtil.getApplicationId(certificateDetails.getSubject());
            Optional<SubscriptionPlan> sOptional=subscriptionPlanRepository.findBySubscriptionId(subscriptionId);
            Optional<Application> appOptional=applicationRepository.findByAppId(Long.valueOf(appId));
            if (!sOptional.isPresent()) {
                ErrorHandlerUtil.handleError(ErrorCode.INVALID_SUBSCRIPTION_ID);
            }
            SubscriptionPlan existingSubscriptionPlan=sOptional.get();
            Timestamp startDate = Timestamp.from(certificateDetails.getValidFrom().toInstant());
            Timestamp endDate = Timestamp.from(certificateDetails.getExpiryDate().toInstant());

            // Calculate months between dates
            LocalDate startLocalDate = startDate.toLocalDateTime().toLocalDate();
            LocalDate endLocalDate = endDate.toLocalDateTime().toLocalDate();
            long monthsBetween = ChronoUnit.MONTHS.between(startLocalDate, endLocalDate);
            existingSubscriptionPlan.setName(ErrorHandlerUtil.getOrganizationName(certificateDetails.getSubject())); // Get organization name
            existingSubscriptionPlan.setDescription(ErrorHandlerUtil.getCommonName(certificateDetails.getSubject())); // Get common name
            existingSubscriptionPlan.setStartDate(startDate);
            existingSubscriptionPlan.setEndDate(endDate);
            existingSubscriptionPlan.setDurationMonths(monthsBetween);
            existingSubscriptionPlan.setCreatedOn(new Date());
            existingSubscriptionPlan.setCreatedBy("system");
            existingSubscriptionPlan.setUpdatedOn(new Date());
            existingSubscriptionPlan.setUpdatedBy("System");



            if (!appOptional.isPresent()) {
                ErrorHandlerUtil.handleError(ErrorCode.INVALID_APP_ID);
            }
            Application existingApplication=appOptional.get();
            existingApplication.setMaxUsers(ErrorHandlerUtil.getMaxUser(certificateDetails.getSubject()));
            existingApplication.setCreatedOn(new Date());
            existingApplication.setCreatedBy("system");
            existingApplication.setUpdatedOn(new Date());
            existingApplication.setUpdatedBy("System");
            // Get the module list from CertificateDetails
            List<String> moduleNames = ErrorHandlerUtil.getModulesList(certificateDetails.getSubject());
            // Initialize a list to store the saved modules
            List<Module> savedModules = new ArrayList<>();
            for (String moduleName : moduleNames) {
                // Check if the module already exists
                Optional<Module> moduleOptional = moduleRepository.findByIdentificationKey(moduleName.trim()); // Use an appropriate method like findByName
                if (moduleOptional.isPresent()) {
                    // Skip to the next iteration if the module already exists
                    continue;
                }

                // Create a new Module instance
                Module newModule = new Module();
                newModule.setApplicationName(existingApplication.getAppName());
                newModule.setName(moduleName.trim()); // Trim to remove unnecessary spaces
                newModule.setIdentificationKey(moduleName.trim()); // Unique key for each module
                newModule.setDescription("Auto-created module");
                newModule.setCreatedOn(new Date());
                newModule.setCreatedBy("system");
                newModule.setUpdatedOn(new Date());
                newModule.setUpdatedBy("system");

                // Save the new module
                Module savedModule = moduleRepository.save(newModule);
                savedModules.add(savedModule); // Add the new module to the savedModules list
            }

            List<Module> existingModules = existingApplication.getModules();
            List<Module> newModulesUp = savedModules;

            if (newModulesUp != null && !newModulesUp.isEmpty()) {
                for (Module newModule : newModulesUp) {
                    Optional<Module> moduleOptional = moduleRepository.findByModuleId(newModule.getModuleId());
                    if (!moduleOptional.isPresent()) {
                        ErrorHandlerUtil.handleError(ErrorCode.INVALID_MODULE_ID); // Or handle the error accordingly
                    }
                    Module module = moduleOptional.get();
                    if (existingModules.stream().anyMatch(existingModule -> existingModule.getModuleId().equals(module.getModuleId()))) {
                        ErrorHandlerUtil.handleError(ErrorCode.MODULE_ALREADY_EXISTS); // Or handle the error accordingly
                    } else {
                        existingModules.add(module);
                        saveUserModule(existingApplication.getOwner(), existingApplication, module);
                    }
                }
            }

            existingApplication.setModules(existingModules);
            applicationRepository.save(existingApplication);
            return subscriptionPlanRepository.save(existingSubscriptionPlan);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private UserModule saveUserModule(UserTable userTable, Application application, Module module) {
        UserModule userModule = new UserModule();
        userModule.setUserTable(userTable);
        userModule.setApplication(application);
        userModule.setModule(module);
        userModule.setUpdatedOn(new Date());
        userModule.setCreatedOn(new Date());
        userModule.setUpdatedBy("System");
        userModule.setCreatedBy("System");
        return userModuleRepository.save(userModule);
    }
}

