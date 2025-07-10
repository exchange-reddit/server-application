package com.omniversity.server.user.ProspectiveUser.dto.request;

import com.omniversity.server.user.entity.Enums.University;
import jakarta.validation.constraints.NotEmpty;

public record DesiredUniversityUpdateDto(
        @NotEmpty(message = "Desired exchange university must be provided")
        University desiredUniversity
) {
}
