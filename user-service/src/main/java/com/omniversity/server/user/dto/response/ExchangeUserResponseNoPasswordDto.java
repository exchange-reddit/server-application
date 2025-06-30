package com.omniversity.server.user.dto.response;

import java.util.Date;

import com.omniversity.server.user.entity.Country;
import com.omniversity.server.user.entity.Language;
import com.omniversity.server.user.entity.Program;
import com.omniversity.server.user.entity.University;

public record ExchangeUserResponseNoPasswordDto(
    String name,
    String privateEmail,
    String userId,
    Date dateOfBirth,
    Date exchangeStart,
    Date exchangeEnd,
    Boolean isAdmin,
    University homeUniversity,
    University exchangeUniversity,
    Program program,
    Country nationality,
    Language preferredLanguage,
    String profilePicture
) {
}
