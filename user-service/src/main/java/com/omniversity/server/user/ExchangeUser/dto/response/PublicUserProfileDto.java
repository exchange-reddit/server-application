package com.omniversity.server.user.ExchangeUser.dto.response;

import com.omniversity.server.user.entity.Enums.University;

import java.time.LocalDate;

public record PublicUserProfileDto(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        University homeUniversity,
        String userName,
        String profilePicture
) implements ReturnDto {}
