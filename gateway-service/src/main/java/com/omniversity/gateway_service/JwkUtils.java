package com.omniversity.gateway_service;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.security.PublicKey;
import java.util.Map;
import java.util.stream.Collectors;

public class JwkUtils {
    @Value("${JWK_SET_URI}")
    private static String jwkSetUri;

    private static Map<String, PublicKey> publicKeys;

    public static void loadJwks() throws Exception {
        URL jwksUrl = new URL(jwkSetUri);
        JWKSet jwkSet = JWKSet.load(jwksUrl);
        publicKeys = jwkSet.getKeys().stream()
                .collect(Collectors.toMap(
                        k -> k.getKeyID(),
                        k -> {
                            try {
                                return k.toRSAKey().toPublicKey();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
    }

    public static PublicKey getPublicKey(String kid) {
        return publicKeys.get(kid);
    }
}
