package com.omniversity.server;

import com.omniversity.server.user.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenProvider {
    // Comment these out to test expiration and refresh logic
    private final long accessTokenExpirationMillis = 3 * 60 * 60 * 1000; // 3 hours
    private final long refreshTokenExpirationMillis = 14L * 24 * 60 * 60 * 1000; // 14 days

    // Uncomment these to test expiration and refresh logic
//    private final long accessTokenExpirationMillis = 3 * 1000; // 3 seconds
//    private final long refreshTokenExpirationMillis = 30 * 1000; // 30 seconds

    private final SecretKey jwtSigningKey;

    @Autowired
    public JwtTokenProvider(SecretKey jwtSigningKey) {
        this.jwtSigningKey = jwtSigningKey;
    }

    // === TOKEN GENERATION ===

    public String generateAccessToken(User user) {
        return generateToken("" + user.getId(), accessTokenExpirationMillis);
    }

    public String generateRefreshToken(User user) {
        return generateToken("" + user.getId(), refreshTokenExpirationMillis);
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

    // check token expired
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpiration(token);
            return expiration.before(new Date());
        } catch (JwtException e) {
            return true; // treat invalid as expired for safety
        }
    }

    // === PARSING TOKEN CLAIMS ===

    public String getSubject(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(jwtSigningKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (ExpiredJwtException ex) {
            // Extract subject from expired token
            return ex.getClaims().getSubject();
        }
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
