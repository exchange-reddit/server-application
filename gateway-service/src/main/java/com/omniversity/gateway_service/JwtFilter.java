package com.omniversity.gateway_service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    @Autowired
    private SecretKey jwtSigningKey;

    public JwtFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. Check for Authorization header
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            // Ensure the header starts with "Bearer "
            if (!authorizationHeader.startsWith("Bearer ")) {
                return onError(exchange, "Invalid Authorization header format: Missing Bearer prefix", HttpStatus.UNAUTHORIZED);
            }

            String jwt = authorizationHeader.substring(7); // Remove "Bearer " prefix

            Claims claims = null;
            try {
                // 2. Validate JWT and extract claims
                claims = Jwts.parser()
                        .verifyWith(jwtSigningKey)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();

                // 3. Check if subject (user ID) is present and not empty
                String subject = claims.getSubject();
                if (subject == null || subject.isEmpty()) {
                    return onError(exchange, "JWT Token is valid but subject (user ID) is missing or empty", HttpStatus.UNAUTHORIZED);
                }

                // 4. Mutate the request to add X-User-Id header
                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-User-Id", subject)
                        .build();

                System.out.println("userid: "+ subject);
                // 5. Pass the mutated request down the filter chain
                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (Exception e) {
                // log.error("JWT validation failed: {}", e.getMessage());
                return onError(exchange, "JWT Token is not valid: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        System.err.println("Gateway JWT Filter Error: " + err);
        // log.error("Gateway JWT Filter Error: {}", err); // Use a proper logger in production
        return response.setComplete();
    }

    public static class Config {
    }

}