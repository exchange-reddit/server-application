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

    public String generateHmac (String result) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(secretKey);
        byte[] hmacBytes = mac.doFinal(result.getBytes());
        return Base64.getEncoder().encodeToString(hmacBytes);
    }
}
