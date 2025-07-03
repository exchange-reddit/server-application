package com.omniversity.server.user.ExchangeUser.dto.request;

import com.omniversity.server.user.entity.Enums.Language;

import java.time.LocalDate;

public record UpdateExchangeUserDto(
        String userName,
        String profilePicture,
        Language preferredLanguage,
        LocalDate exchangeEnd
) {
}
