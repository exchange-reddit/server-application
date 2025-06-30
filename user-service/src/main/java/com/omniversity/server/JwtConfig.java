package com.omniversity.server;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.nimbusds.jose.jwk.JWKSet;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
//    @Value("${jwt.secret}")
//    private String jwtSecretKey;
//
//    @Bean
//    public SecretKey jwtSigningKey() {
//        SecretKey key =  Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
//        return key;
//    }

    // gets privateKey from the private.pem file.
    // dockerfile needs to explicitly move the key to the docker working directory to read the file.
    @Bean
    public static PrivateKey privateKey() throws Exception {
        String key = Files.readString(Paths.get("/app/private.pem"));// for docker environment
        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    @Bean
    public static PublicKey publicKey() throws Exception {

        PublicKey publicKey = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(
                        Base64.getDecoder().decode(
                                Files.readString(Paths.get("/app/public.pem"))
                                        .replace("-----BEGIN PUBLIC KEY-----", "")
                                        .replace("-----END PUBLIC KEY-----", "")
                                        .replaceAll("\\s", "")
                        )
                ));

        return publicKey;
    }
}
