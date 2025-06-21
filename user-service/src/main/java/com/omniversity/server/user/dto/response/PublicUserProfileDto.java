package com.omniversity.server.user.dto.response;

import com.omniversity.server.user.entity.University;

import java.util.Date;

public record PublicUserProfileDto(
        String name,
        Date dateOfBirth,
        University university,
        String userId,
        String profilePicture
) implements ReturnDto {}
