package com.omniversity.gateway_service;

import java.security.PublicKey;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
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
    public JwtHeaderGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = extractToken(exchange);
            if (token == null) {
                return onError(exchange, "Missing or malformed Authorization header", HttpStatus.UNAUTHORIZED);
            }

            try {
                log.debug("Parsing JWT token...");

                JwsHeader header = Jwts.parser()
                        .build()
                        .parseSignedClaims(token)
                        .getHeader();

                String kid = header.getKeyId();
                log.info("JWT Key ID (kid): {}", kid);

                PublicKey publicKey = JwkUtils.getPublicKey(kid);
                if (publicKey == null) {
                    log.error("No public key found for kid: {}", kid);
                    return onError(exchange, "Public key not found for kid", HttpStatus.UNAUTHORIZED);
                }

                Claims claims = Jwts.parser()
                        .verifyWith(publicKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String userId = claims.getSubject();
                String userType = claims.get("userType", String.class);

                if (userId == null || userId.isEmpty()) {
                    return onError(exchange, "JWT subject (user ID) missing", HttpStatus.UNAUTHORIZED);
                }

                log.info("JWT validated. userId: {}, userType: {}", userId, userType);
                log.debug("All claims: {}", claims);

                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", userId)
                        .header("X-User-Type", userType)
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (JwtException | IllegalArgumentException e) {
                log.error("JWT validation failed: {}", e.getMessage(), e);
                return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header missing or invalid");
            return null;
        }
        log.debug("Authorization header: {}", authHeader);
        return authHeader.substring(7);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        log.warn("JWT Filter Error: {}", message);
        return response.setComplete();
    }

    public static class Config {
        // Placeholder for potential future config values
    }
}
