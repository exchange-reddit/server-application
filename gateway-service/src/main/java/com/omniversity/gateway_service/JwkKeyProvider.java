package com.omniversity.gateway_service;

import com.nimbusds.jose.jwk.JWKSet;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.PublicKey;

@Component
@Slf4j
public class JwkKeyProvider {

    @Value("${JWK_SET_URI}")
    private String jwkSetUri;

    private volatile PublicKey currentPublicKey;

    @PostConstruct
    public void init() {
        log.info("Initializing JWKS public key loader...");
        reload();
    }

    /**
     * Reloads the JWKS public key every 24 hours.
     */
    @Scheduled(fixedRate = 86_400_000)
    public void reload() {
        try {
            log.info("Fetching JWKS from: {}", jwkSetUri);

            JWKSet jwkSet = JWKSet.load(new URL(jwkSetUri));
            var jwk = jwkSet.getKeyByKeyId("gateway-key");

            if (jwk == null) {
                log.warn("No key with keyId 'gateway-key' found in JWKS.");
                return;
            }

            currentPublicKey = jwk.toRSAKey().toPublicKey();
            log.info("Successfully loaded public key with keyId 'gateway-key'");

        } catch (Exception e) {
            log.error("Failed to reload JWKS from URI: {}", jwkSetUri, e);
        }
    }

    public PublicKey getPublicKey() {
        if (currentPublicKey == null) {
            log.warn("Public key is not yet loaded or is null");
        }
        return currentPublicKey;
    }
}
