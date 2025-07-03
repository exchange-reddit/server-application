package com.omniversity.server.user.ExchangeUser.dto.response;

import com.omniversity.server.user.entity.Enums.Country;
import com.omniversity.server.user.entity.Enums.Language;
import com.omniversity.server.user.entity.Enums.Program;
import com.omniversity.server.user.entity.Enums.University;

import java.time.LocalDate;

public record ExchangeUserResponseNoPasswordDto(
    String firstName,
    String lastName,
    String privateEmail,
    String userName,
    LocalDate dateOfBirth,
    LocalDate exchangeStart,
    LocalDate exchangeEnd,
    University homeUniversity,
    University exchangeUniversity,
    Program program,
    Country nationality,
    Language preferredLanguage,
    String profilePicture
) {
}
