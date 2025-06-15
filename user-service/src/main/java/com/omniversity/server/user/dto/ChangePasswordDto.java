package com.omniversity.server.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDto(
        @NotBlank(message = "Current password must be provided")
        String currentPassword,

        @NotBlank(message = "New password must be provided")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String newPassword
) {
}
