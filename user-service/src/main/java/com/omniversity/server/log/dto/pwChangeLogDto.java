package com.omniversity.server.log.dto;

import com.omniversity.server.log.entity.AuditType;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @param updateUser: ID of the user performing the audit
 * @param updateContent: Previous password hash before audit request
 */

public record pwChangeLogDto (
        @NotBlank(message = "Update user must be provided")
        String updateUser,

        @NotBlank(message = "Update content must be provided")
        String updateContent
) {
}
