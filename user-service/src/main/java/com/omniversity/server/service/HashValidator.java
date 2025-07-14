package com.omniversity.server.service;

import com.omniversity.server.user.exception.HashValidationFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

@Component
public class HashValidator {

    @Value("${verification.secret}")
    private String secret;

    public String generateHash(String input) throws HashValidationFailedException {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKey);
            byte[] hash = mac.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new HashValidationFailedException("Hash generation failed");
        }
    }

    public boolean validateHash(String hashValue, String rawValue) throws HashValidationFailedException {
        try {
            String expectedHash = generateHash(rawValue);
            return Objects.equals(hashValue, expectedHash);
        } catch (HashValidationFailedException e) {
            throw new HashValidationFailedException("Failed to generate or validate hash");
        }
    }
}
