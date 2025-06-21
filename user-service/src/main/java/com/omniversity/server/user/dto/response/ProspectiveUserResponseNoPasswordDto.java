package com.omniversity.server.user.dto.response;

import com.omniversity.server.user.entity.Country;
import com.omniversity.server.user.entity.Language;
import com.omniversity.server.user.entity.Program;
import com.omniversity.server.user.entity.University;

import java.util.Date;

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
