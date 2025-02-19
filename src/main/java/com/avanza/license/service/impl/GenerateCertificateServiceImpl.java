package com.avanza.license.service.impl;

import com.avanza.license.service.GenerateCertificateService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class GenerateCertificateServiceImpl implements GenerateCertificateService {
    @Override
    public String generateKeys(
            String passphrase,
            String appId,
            String userId,
            String moduleId,
            String maxUser,
            String licenseKey,
            int validityDays
    ) {
        try {
            // Step 1: Create a unique directory for each request
            String folderName = "certs_" + UUID.randomUUID();
            Path folderPath = Paths.get(folderName);
            Files.createDirectories(folderPath);

            // File paths
            String privateKeyPath = folderPath.resolve("private.key").toString();
            String decryptedKeyPath = folderPath.resolve("decrypted.key").toString();
            String publicKeyPath = folderPath.resolve("publicKey.pem").toString();
            String csrPath = folderPath.resolve("request.csr").toString();
            String opensslConfigPath = "C:\\\\Program Files\\\\OpenSSL-Win64\\\\bin\\\\cnf\\\\openssl.cnf";
            String certificatePath = folderPath.resolve("certificate.crt").toString();
            String keystoreP12Path = folderPath.resolve("keystore.p12").toString();
            String keystoreJKSPath = folderPath.resolve("keystore.jks").toString();

            // Step 2: Generate Private Key
            String privateKeyResult = executeCommandWithPassphrase(privateKeyPath, passphrase);
            if (!privateKeyResult.contains("successfully")) {
                return "Error generating private key: " + privateKeyResult;
            }

            // Step 3: Generate Decrypted Key
            String decryptedKeyResult = executeCommandWithPassphraseForDecrypted(privateKeyPath, decryptedKeyPath, passphrase);
            if (!decryptedKeyResult.contains("successfully")) {
                return "Error generating decrypted key: " + decryptedKeyResult;
            }

            // Step 4: Generate Public Key
            String publicKeyResult = executeCommandForPublicKey(decryptedKeyPath, publicKeyPath);
            if (!publicKeyResult.contains("successfully")) {
                return "Error generating public key: " + publicKeyResult;
            }

            // Step 5: Generate CSR (Certificate Signing Request)
            String csrCommand = "openssl req -new -key \"" + decryptedKeyPath + "\" -out \"" + csrPath + "\" -config \"" + opensslConfigPath + "\" " +
                    "-subj \"/C=qw/ST=wq/L=ds/O=ds/OU=cxds/CN=sa/emailAddress=sa/" +
                    "1.2.3.4.5.1000=" + appId + "/" +
                    "1.2.3.4.5.1001=" + userId + "/" +
                    "1.2.3.4.5.1002=" + moduleId + "/" +
                    "1.2.3.4.5.1003=" + maxUser + "/" +
                    "1.2.3.4.5.1004=" + licenseKey + "\"";

            String csrResult = executeCommand(csrCommand);
            if (!csrResult.contains("successfully")) {
                return "Error generating CSR: " + csrResult;
            }

            // Step 6: Generate Certificate
            String certificateCommand = "openssl x509 -req " +
                    "-days " + validityDays + " " +
                    "-in \"" + csrPath + "\" " +
                    "-signkey \"" + decryptedKeyPath + "\" " +
                    "-out \"" + certificatePath + "\"";

            String certificateResult = executeCommand(certificateCommand);
            if (!certificateResult.contains("successfully")) {
                return "Error generating certificate: " + certificateResult;
            }

            // Step 7: Generate P12 Keystore
            String password = "password"; // Set your password here
            String p12Command = "openssl pkcs12 -export -in \"" + certificatePath + "\" -inkey \"" + decryptedKeyPath + "\" -out \"" + keystoreP12Path + "\" " +
                    "-name myalias -keypbe PBE-SHA1-3DES -certpbe PBE-SHA1-3DES -macalg SHA1 -passout pass:" + password;

            String p12Result = executeCommand(p12Command);
            if (!p12Result.contains("successfully")) {
                return "Error generating P12 keystore: " + p12Result;
            }

            // Step 8: Generate JKS Keystore
            String jksCommand = "keytool -importkeystore -deststorepass changeit -destkeypass changeit -destkeystore \"" + keystoreJKSPath + "\" " +
                    "-srckeystore \"" + keystoreP12Path + "\" -srcstoretype PKCS12 -srcstorepass " + password + " -alias myalias";

            String jksResult = executeCommand(jksCommand);
            if (!jksResult.contains("successfully")) {
                return "Error generating JKS keystore: " + jksResult;
            }
            String checkSumResult = executeCommandChecksum("openssl dgst -sha256 " + keystoreJKSPath);
            if (!checkSumResult.startsWith("Checksum generated successfully")) {
                return "Error generating CheckSum file: " + checkSumResult;
            }
            return "Keys, CSR, certificate, P12 keystore, and JKS keystore and CheckSum generated successfully in: " + folderPath.toAbsolutePath();
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }

    private String executeCommandWithPassphrase(String privateKeyPath, String passphrase) throws IOException, InterruptedException {
        String command = "openssl genpkey -algorithm RSA -out \"" + privateKeyPath + "\" -aes256 -pass pass:" + passphrase;
        return executeCommand(command);
    }

    private String executeCommandWithPassphraseForDecrypted(String privateKeyPath, String decryptedKeyPath, String passphrase) throws IOException, InterruptedException {
        String command = "openssl rsa -in \"" + privateKeyPath + "\" -out \"" + decryptedKeyPath + "\" -passin pass:" + passphrase;
        return executeCommand(command);
    }

    private String executeCommandForPublicKey(String decryptedKeyPath, String publicKeyPath) throws IOException, InterruptedException {
        String command = "openssl rsa -in \"" + decryptedKeyPath + "\" -pubout -out \"" + publicKeyPath + "\"";
        return executeCommand(command);
    }

    private String executeCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        return (exitCode == 0) ? "Command executed successfully" : "Command failed with exit code " + exitCode;
    }

    private static String executeCommandChecksum(String command) throws IOException, InterruptedException {
        System.out.println("Start executing command....");

        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(new java.io.File("."));  // Sets working directory to current location
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        }

        // Wait for process to complete
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            return "Command failed with exit code " + exitCode + " and output: " + output.toString().trim();
        }

        String outputStr = output.toString().trim();
        String checksum = "";

        if (outputStr.contains("=")) {
            checksum = outputStr.substring(outputStr.indexOf('=') + 1).trim();
        }

        if (!checksum.isEmpty()) {
            // Extract JKS file path correctly
            String jksFilePath = command.replaceAll(".*openssl dgst -sha256 \"?(.*?)\"?$", "$1");
            Path jksPath = Paths.get(jksFilePath);
            Path checksumFilePath = jksPath.getParent().resolve("checksum.txt");

            // Write checksum to file
            try (BufferedWriter writer = Files.newBufferedWriter(checksumFilePath)) {
                writer.write(checksum);
                writer.newLine();
                System.out.println("Checksum generated successfully: " + checksum);
                return "Checksum generated successfully: " + checksum;
            } catch (IOException e) {
                System.err.println("Error writing checksum to file: " + e.getMessage());
                return "Error writing checksum to file: " + e.getMessage();
            }
        } else {
            System.err.println("Checksum not found in the command output.");
            return "Checksum not found in the command output.";
        }
    }
}
