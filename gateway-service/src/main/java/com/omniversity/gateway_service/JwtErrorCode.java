package com.omniversity.gateway_service;

/**
 * Enum representing specific error codes for JWT validation in the Gateway.
 */
public enum JwtErrorCode {

    // Token is missing or the Authorization header is malformed
    MISSING_OR_MALFORMED_HEADER,

    // Public key couldn't be retrieved from the JWK provider
    PUBLIC_KEY_NOT_FOUND,

    // The JWT has expired, causes the client to make a refresh token request
    TOKEN_EXPIRED,

    // The JWT uses an unsupported format or algorithm
    TOKEN_UNSUPPORTED,

    // The JWT is malformed (e.g., corrupted, invalid structure)
    TOKEN_MALFORMED,

    // Catch-all for general JWT parsing/validation failure
    TOKEN_INVALID,

    // Subject (`sub` claim) is missing in a valid token
    SUBJECT_MISSING
}
