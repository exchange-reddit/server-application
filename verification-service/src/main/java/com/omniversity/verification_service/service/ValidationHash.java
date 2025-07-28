package com.omniversity.verification_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class ValidationHash {

    @Value("${verification.secret}")
    private String secret;

    // Method that returns a hash value derived from the email that was used for validation
    public String generateHmac (String result) throws Exception {
        // Using the SHA-256 algorithm to generate hash values (Check HMAC documentation for further information)
        Mac mac = Mac.getInstance("HmacSHA256");
        // Use the secret value stored in .env file
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(secretKey);
        byte[] hmacBytes = mac.doFinal(result.getBytes());
        // Return the HMAC code value in a string format
        return Base64.getEncoder().encodeToString(hmacBytes);
    }
}
