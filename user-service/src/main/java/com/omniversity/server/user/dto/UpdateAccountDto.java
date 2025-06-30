package com.omniversity.server.user.dto;

import java.util.Date;

import com.omniversity.server.user.entity.Language;

public record UpdateAccountDto(
        String userId,
        String profilePicture,
        Language preferredLanguage,
        Date exchangeEnd
) {
}
