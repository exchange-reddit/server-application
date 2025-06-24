package com.omniversity.server.user.dto;

import com.omniversity.server.user.entity.Country;
import jakarta.validation.constraints.NotNull;

public record ChangeNationalityDto(
        @NotNull(message = "New nationality must be provided")
        Country nationality
) {
}
