package com.avanza.license.util;

import com.avanza.license.Dto.CertificateDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;


@RestController
@RequestMapping("/v1")
public class TestController {
    @RequestMapping("/test")
    public String test() {
        return "License Management Module is  running successfully!";
    }

    @Value("${file.upload-dir}")  // Read the directory path from application.properties
    private String uploadDir;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Step 1: Get the original file name
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                return "Invalid file name.";
            }

            // Step 2: Create the path for the new file
            Path filePath = Paths.get(uploadDir + "/" + originalFileName);

            // Step 3: Delete the previous file if it exists
            File previousFile = filePath.toFile();
            if (previousFile.exists()) {
                previousFile.delete();
            }

            // Step 4: Save the new file with its original name
            Files.write(filePath, file.getBytes());

            return "File uploaded successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading file: " + e.getMessage();
        }
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

            return new ResponseEntity<>("Certificate uploaded successfully! Details: \n" + certificateDetails, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/generate-client-key")
    public String generateClientKey() {
        try {
            // Fetch BIOS UUID
            String biosUuid = getBiosUuid();
            System.out.println("Bios ID:" + biosUuid);

            // Fetch Ethernet MAC Address
            String ethernetMac = getEthernetMacAddress();
//          String macAddress=ethernetMac.replace(":", "-");
            System.out.println("ethernetMac:" + ethernetMac);


            // Combine BIOS UUID and Ethernet MAC
            String combinedData = biosUuid + ethernetMac;


            // Generate SHA-256 hash
            return generateSha256Hash(combinedData);
        } catch (Exception e) {
            return "Error generating client key: " + e.getMessage();
        }
    }

    private String getBiosUuid() throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        Process process;
        String biosUuid = "UNKNOWN_UUID";  // Default if UUID retrieval fails

        if (os.contains("win")) {
            process = Runtime.getRuntime().exec("wmic csproduct get UUID");
        } else if (os.contains("nix") || os.contains("nux")) {
            // Ensure the process has sufficient privileges to run 'dmidecode'
            process = Runtime.getRuntime().exec("sudo dmidecode -s system-uuid");
        } else {
            throw new UnsupportedOperationException("Unsupported OS: " + os);
        }

        int exitCode = process.waitFor();  // Wait for process to finish
        if (exitCode != 0) {
            throw new RuntimeException("Failed to retrieve BIOS UUID, process exit code: " + exitCode);
        }

        // Read the command output to get the UUID
        try (Scanner scanner = new Scanner(process.getInputStream())) {
            // Skip the header line (first line)
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Skip the "UUID" header
            }

            // Check if there is a second line with the UUID (skip any blank lines or extra spaces)
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    biosUuid = line;  // Capture the UUID from the second non-empty line
                    break;
                }
            }

            // Debugging output
            System.out.println("Found BIOS UUID: " + biosUuid);
        }

        return biosUuid;
    }

    private String getEthernetMacAddress() throws Exception {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            byte[] mac = networkInterface.getHardwareAddress();

            // Check if MAC address exists and skip virtual interfaces
            if (mac != null && !networkInterface.isVirtual() && networkInterface.getName().contains("eth")) {
                StringBuilder macAddress = new StringBuilder();
                for (byte b : mac) {
                    macAddress.append(String.format("%02X:", b));
                }
                return macAddress.substring(0, macAddress.length() - 1); // Remove trailing colon
            }
        }
        return "UNKNOWN_MAC";
    }
    private String generateSha256Hash(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes("UTF-8"));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}