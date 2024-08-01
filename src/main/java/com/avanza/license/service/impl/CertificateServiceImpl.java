package com.avanza.license.service.impl;

import com.avanza.license.Dto.CertificateDetails;
import com.avanza.license.Enum.ErrorCode;
import com.avanza.license.entity.SubscriptionPlan;
import com.avanza.license.repositories.SubscriptionPlanRepository;
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
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

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

}
