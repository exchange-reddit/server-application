package com.omniversity.server.log.dto;

import jakarta.validation.constraints.NotBlank;

public record accountDeleteLogDto(
        @NotBlank(message = "Update user must be provided")
        String updateUser,

        @NotBlank(message = "Update content must be provided")
        String updateContent
) {
}
