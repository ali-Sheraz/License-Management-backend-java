package com.avanza.license.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CertificateDetails {
    private String subject;
    private String issuer;
    private String serialNumber;
    private Date validFrom;
    private Date expiryDate;
    private String signatureAlgorithm;
    private int version;
    private String publicKey;
    private String pemEncodedCertificate;
    
}

