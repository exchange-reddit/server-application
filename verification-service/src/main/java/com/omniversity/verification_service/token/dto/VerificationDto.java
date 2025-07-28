package com.omniversity.verification_service.token.dto;

import jakarta.validation.constraints.*;

public record VerificationDto(
        @NotBlank(message = "Verification code must be provided")
        String code,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email used for verification must be provided")
        String email,

        @NotBlank(message = "Valid verification type must be provided")
        int verificationType
) {
}
