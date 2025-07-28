package com.omniversity.server.user.ExchangeUser.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ValidationDto(

        @NotBlank(message = "User ID must be provided")
        long id,

        @NotBlank(message = "Email address must be provided")
        String email,

        @NotBlank(message = "Code must be provided")
        String code,

        @NotBlank(message = "Email Type must be provided")
        int emailType


) {
}
