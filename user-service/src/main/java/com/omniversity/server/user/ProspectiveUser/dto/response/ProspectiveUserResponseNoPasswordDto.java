package com.omniversity.server.user.ProspectiveUser.dto.response;

import com.omniversity.server.user.entity.Enums.Country;
import com.omniversity.server.user.entity.Enums.Language;
import com.omniversity.server.user.entity.Enums.Program;
import com.omniversity.server.user.entity.Enums.University;

import java.time.LocalDate;

public record ProspectiveUserResponseNoPasswordDto(
        String firstName,
        String lastName,
        String privateEmail,
        String userName,
        LocalDate dateOfBirth,
        University homeUniversity,
        University desiredUniversity,
        Program program,
        Country nationality,
        Language preferredLanguage,
        String profilePicture
) {
}
