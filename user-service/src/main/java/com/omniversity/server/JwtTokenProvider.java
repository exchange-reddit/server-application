package com.omniversity.server;

import com.omniversity.server.user.entity.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

public class JwtTokenProvider {
    private final long accessTokenExpirationMillis = 3 * 60 * 60 * 1000; // 3 hours
    private final long refreshTokenExpirationMillis = 14L * 24 * 60 * 60 * 1000; // 14 days

    private final SecretKey jwtSigningKey;

    @Autowired
    public JwtTokenProvider(SecretKey jwtSigningKey) {
        this.jwtSigningKey = jwtSigningKey;
    }

    // === TOKEN GENERATION ===

    public String generateAccessToken(User user) {
        return generateToken(user.getUserId(), accessTokenExpirationMillis);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user.getUserId(), refreshTokenExpirationMillis);
    }

    private String generateToken(String subject, long expirationMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(jwtSigningKey)
                .compact();
    }

    // === TOKEN VALIDATION ===

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(jwtSigningKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // === PARSING TOKEN CLAIMS ===

    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(jwtSigningKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Date getExpiration(String token) {
        return Jwts.parser()
                .verifyWith(jwtSigningKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
}
