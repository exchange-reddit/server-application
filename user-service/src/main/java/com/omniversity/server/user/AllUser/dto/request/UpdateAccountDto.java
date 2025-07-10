package com.omniversity.server.user.AllUser.dto.request;

import com.omniversity.server.user.entity.Enums.Language;

import java.time.LocalDate;

public record UpdateAccountDto(
        String userId,
        String profilePicture,
        Language preferredLanguage,
        LocalDate exchangeEnd
) {
}
