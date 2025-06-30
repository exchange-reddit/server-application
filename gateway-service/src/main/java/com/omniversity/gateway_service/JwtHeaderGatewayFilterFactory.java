package com.omniversity.gateway_service;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtHeaderGatewayFilterFactory.Config> {

    // injected
    private final JwkKeyProvider jwkKeyProvider;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JwtHeaderGatewayFilterFactory(JwkKeyProvider jwkKeyProvider) {
        super(Config.class);
        this.jwkKeyProvider = jwkKeyProvider;
    }


    /**
     * filters jwt token claims (userId, userType), and enrich http headers.
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = extractToken(exchange);
            String path = exchange.getRequest().getURI().getPath();

            if (token == null) {
                log.warn("JWT missing or malformed. Path: {}", path);
                return onError(exchange, "Missing or malformed Authorization header", HttpStatus.UNAUTHORIZED,
                        JwtErrorCode.MISSING_OR_MALFORMED_HEADER.name());
            }

            try {
                log.debug("Attempting to parse and validate JWT. Path: {}", path);

                PublicKey publicKey = jwkKeyProvider.getPublicKey();
                if (publicKey == null) {
                    log.error("Public key unavailable. Path: {}", path);
                    return onError(exchange, "Public key not found", HttpStatus.UNAUTHORIZED,
                            JwtErrorCode.PUBLIC_KEY_NOT_FOUND.name());
                }

                // verify and parse using public key
                Jws<Claims> jws = Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token);

                JwsHeader header = jws.getHeader();
                Claims claims = jws.getPayload();

                String userId = claims.getSubject();
                String userType = claims.get("userType", String.class);

                if (userId == null || userId.isEmpty()) {
                    log.warn("JWT subject missing. Path: {}, Claims: {}", path, claims);
                    return onError(exchange, "JWT subject (user ID) missing", HttpStatus.UNAUTHORIZED,
                            JwtErrorCode.SUBJECT_MISSING.name());
                }

                log.info("JWT validated. userId: {}, userType: {}, path: {}", userId, userType, path);
                log.debug("JWT header: {}, claims: {}", header, claims);

                // adding http headers
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().header("X-User-Id", userId).header(
                        "X-User-Type", userType).build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (ExpiredJwtException e) {
                log.warn("JWT expired. Path: {}, Reason: {}", path, e.getMessage());
                // Client should attempt token refresh
                return onError(exchange, "JWT token has expired", HttpStatus.UNAUTHORIZED,
                        JwtErrorCode.TOKEN_EXPIRED.name());
            } catch (UnsupportedJwtException e) {
                log.warn("Unsupported JWT token. Path: {}, Reason: {}", path, e.getMessage());
                // Client likely needs to re-authenticate as the token format is unknown
                return onError(exchange, "JWT token is unsupported", HttpStatus.UNAUTHORIZED,
                        JwtErrorCode.TOKEN_UNSUPPORTED.name());
            } catch (MalformedJwtException e) {
                log.warn("Malformed JWT token. Path: {}, Reason: {}", path, e.getMessage());
                // Client likely needs to re-authenticate as the token is structurally invalid
                return onError(exchange, "JWT token is malformed", HttpStatus.UNAUTHORIZED,
                        JwtErrorCode.TOKEN_MALFORMED.name());
            } catch (JwtException | IllegalArgumentException e) {
                log.error("JWT validation failed. Path: {}, Reason: {}", path, e.getMessage(), e);
                // General invalid token, client might try refreshing but likely needs re-auth
                return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED,
                        JwtErrorCode.TOKEN_INVALID.name());
            }
        };
    }

    /**
     * extracts token by parsing the http header and selecting what comes after:
     * "Authorization: Bearer"
     */
    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String path = exchange.getRequest().getURI().getPath();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header missing or improperly formatted. Path: {}", path);
            return null;
        }

        log.debug("Authorization header received. Path: {}", path);

        // 7 because of the string "Bearer " with the space included
        return authHeader.substring(7);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status, String errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> errorBody = Map.of("timestamp", Instant.now().toString(), "status", status.value(),
                "error", status.getReasonPhrase(), "message", message, "code", errorCode);

        log.warn("Request rejected. Reason: {}, Code: {}, Status: {}", message, errorCode, status.value());

        DataBufferFactory bufferFactory = response.bufferFactory();
        try {
            byte[] bodyBytes = objectMapper.writeValueAsBytes(errorBody);
            DataBuffer buffer = bufferFactory.wrap(bodyBytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize error response: {}", e.getMessage(), e);
            // Fallback to a simple plain-text error
            byte[] fallbackBytes = String.format("{\"error\": \"%s\"}", message).getBytes(StandardCharsets.UTF_8);
            DataBuffer fallbackBuffer = bufferFactory.wrap(fallbackBytes);
            return response.writeWith(Mono.just(fallbackBuffer));
        }
    }


    public static class Config {
        // For future use
    }
}

