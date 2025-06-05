package com.omniversity.server;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Bean
    public SecretKey jwtSigningKey() {
        SecretKey key =  Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
        return key;
    }
}
