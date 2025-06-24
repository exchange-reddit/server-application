package com.omniversity.server.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * About:
 * Extract information from the client requests' header.
 */

@Component
public class HeaderExtractor {
    public String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");

        if (xForwardedForHeader == null || xForwardedForHeader.isEmpty() || "unknown".equalsIgnoreCase(xForwardedForHeader)) {
            return request.getRemoteAddr();
        } else {
            String[] ips = xForwardedForHeader.split(",");
            return ips[0].trim();
        }
    }
}
