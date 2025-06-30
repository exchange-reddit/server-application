package com.omniversity.gateway_service;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtHeaderGatewayFilterFactory.Config> {

    private final JwkKeyProvider jwkKeyProvider;

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
                return onError(exchange, "Missing or malformed Authorization header", HttpStatus.UNAUTHORIZED);
            }

            try {
                log.debug("Attempting to parse and validate JWT. Path: {}", path);

                PublicKey publicKey = jwkKeyProvider.getPublicKey();
                if (publicKey == null) {
                    log.error("Public key unavailable. Path: {}", path);
                    return onError(exchange, "Public key not found", HttpStatus.UNAUTHORIZED);
                }

                // verify and parse using public key
                Jws<Claims> jws = Jwts.parser()
                        .verifyWith(publicKey)
                        .build()
                        .parseSignedClaims(token);

                JwsHeader header = jws.getHeader();
                Claims claims = jws.getPayload();

                String userId = claims.getSubject();
                String userType = claims.get("userType", String.class);

                if (userId == null || userId.isEmpty()) {
                    log.warn("JWT subject missing. Path: {}, Claims: {}", path, claims);
                    return onError(exchange, "JWT subject (user ID) missing", HttpStatus.UNAUTHORIZED);
                }

                log.info("JWT validated. userId: {}, userType: {}, path: {}", userId, userType, path);
                log.debug("JWT header: {}, claims: {}", header, claims);

                // adding http headers
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", userId)
                        .header("X-User-Type", userType)
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (ExpiredJwtException e) {
                log.warn("JWT expired. Path: {}, Reason: {}", path, e.getMessage());
                return onError(exchange, "JWT token has expired", HttpStatus.UNAUTHORIZED);
            } catch (UnsupportedJwtException e) {
                log.warn("Unsupported JWT token. Path: {}, Reason: {}", path, e.getMessage());
                return onError(exchange, "JWT token is unsupported", HttpStatus.UNAUTHORIZED);
            } catch (MalformedJwtException e) {
                log.warn("Malformed JWT token. Path: {}, Reason: {}", path, e.getMessage());
                return onError(exchange, "JWT token is malformed", HttpStatus.UNAUTHORIZED);
            } catch (JwtException | IllegalArgumentException e) {
                log.error("JWT validation failed. Path: {}, Reason: {}", path, e.getMessage(), e);
                return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
            }
        };
    }

     /**
      * extracts token by parsing the http header and selecting what comes after:
     "Authorization: Bearer"
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

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");

        String jsonBody = String.format("{\"error\": \"%s\"}", message);
        DataBuffer buffer = response.bufferFactory().wrap(jsonBody.getBytes(StandardCharsets.UTF_8));

        log.warn("JWT Filter rejected request. Reason: {}, Status: {}", message, status.value());
        return response.writeWith(Mono.just(buffer));
    }

    public static class Config {
        // For future use
    }
}

