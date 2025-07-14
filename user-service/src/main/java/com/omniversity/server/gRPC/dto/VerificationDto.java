package com.omniversity.server.gRPC.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerificationDto(
        @NotBlank(message = "User Id must be provided")
        long id,

        @NotBlank(message = "Verification code must be provided")
        String code,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email used for verification must be provided")
        String email,

        @NotBlank(message = "Valid verification type must be provided")
        int verificationType
) {
}
