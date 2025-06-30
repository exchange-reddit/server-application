package com.omniversity.server.user.dto.response;

import java.util.Date;

import com.omniversity.server.user.entity.Country;
import com.omniversity.server.user.entity.Language;
import com.omniversity.server.user.entity.Program;
import com.omniversity.server.user.entity.University;

public record ProspectiveUserResponseNoPasswordDto (
        String name,
        String privateEmail,
        String userId,
        Date dateOfBirth,
        Boolean isAdmin,
        University homeUniversity,
        Country nationality,
        Program program,
        Language preferredLanguage,
        String profilePicture
) {
}
