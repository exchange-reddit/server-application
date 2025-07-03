package com.omniversity.server.user.ExchangeUser.dto.response;

import com.omniversity.server.user.entity.Enums.Program;
import com.omniversity.server.user.entity.Enums.University;

public record FriendSuggestionDto(
        String firstName,
        String lastName,
        String profilePicture,
        University homeUniversity,
        Program program

) implements ReturnDto {
}
