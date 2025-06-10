package com.omniversity.verification_service.token.dto;

import jakarta.validation.constraints.*;

public record RegistrationDto(
        @NotBlank(message="Name cannot be blank")
        String name,

        @NotBlank(message="Email address must be provided")
        @Email(message="Email must be valid")
        String email,

        @NotBlank(message="Verification type must be set")
        int verificationType


) {
}
