package com.omniversity.server.user.dto;

import com.omniversity.server.user.entity.Language;

import java.util.Date;

public record UpdateAccountDto(
        String userId,
        String profilePicture,
        Language preferredLanguage,
        Date exchangeEnd
) {
}
