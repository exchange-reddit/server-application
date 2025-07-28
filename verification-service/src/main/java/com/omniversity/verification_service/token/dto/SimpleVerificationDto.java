package com.omniversity.verification_service.token.dto;


public record SimpleVerificationDto(
        boolean result,
        String message
) {
}
