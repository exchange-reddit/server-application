package com.omniversity.server;

import static com.omniversity.server.JwtConfig.privateKey;
import static com.omniversity.server.JwtConfig.publicKey;

import com.omniversity.server.user.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${USER_SERVICE_URL}")
    private String userServiceUrl;

    // Comment these out to test expiration and refresh logic
    private final long accessTokenExpirationMillis = 3 * 60 * 60 * 1000; // 3 hours
    private final long refreshTokenExpirationMillis = 14L * 24 * 60 * 60 * 1000; // 14 days

    // Uncomment these to test expiration and refresh logic
//    private final long accessTokenExpirationMillis = 3 * 1000; // 3 seconds
//    private final long refreshTokenExpirationMillis = 30 * 1000; // 30 seconds


    @Autowired
    public JwtTokenProvider() {

    }

    // === TOKEN GENERATION ===

    public String generateAccessToken(User user) throws Exception {
        return generateToken(user, accessTokenExpirationMillis);
    }

    public String generateRefreshToken(User user) throws Exception {
        return generateToken(user, refreshTokenExpirationMillis);
    }

    private String generateToken(User user, long expirationMillis) throws Exception {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);
        String subject = "" + user.getId();
        String userType = "" + user.getUserType();
        return Jwts.builder()
                .subject(subject)
                .claim("userType", userType)
                .issuedAt(now)
                .expiration(expiry)
                .issuer(userServiceUrl)
                .signWith(privateKey())
                .compact();
    }

    // === TOKEN VALIDATION ===

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(publicKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // === PARSING TOKEN CLAIMS ===

    public String getSubject(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (ExpiredJwtException ex) {
            // Extract subject from expired token
            return ex.getClaims().getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Date getExpiration(String token) throws Exception {
        return Jwts.parser()
                .verifyWith(publicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
}
