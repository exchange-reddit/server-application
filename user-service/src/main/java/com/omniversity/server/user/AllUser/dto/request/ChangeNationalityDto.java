package com.omniversity.server.user.AllUser.dto.request;

import com.omniversity.server.user.entity.Enums.Country;
import jakarta.validation.constraints.NotNull;

public record ChangeNationalityDto(
        @NotNull(message = "New nationality must be provided")
        Country nationality
) {
}
