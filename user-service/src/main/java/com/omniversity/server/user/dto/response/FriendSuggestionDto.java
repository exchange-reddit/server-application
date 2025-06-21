package com.omniversity.server.user.dto.response;

import com.omniversity.server.user.entity.Program;
import com.omniversity.server.user.entity.University;

public record FriendSuggestionDto(
        String name,
        String profilePicture,
        University homeUniversity,
        Program program

) implements ReturnDto {
}
