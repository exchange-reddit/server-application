package com.omniversity.server.gRPC.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequestDto(
        @NotBlank(message="Name cannot be blank")
        String name,

        @NotBlank(message="Email address must be provided")
        @Email(message="Email must be valid")
        String email,

        @NotBlank(message="Verification type must be set")
        int verificationType


) {
}
