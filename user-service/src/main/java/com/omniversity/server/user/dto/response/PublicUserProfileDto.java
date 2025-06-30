package com.omniversity.server.user.dto.response;

import java.util.Date;

import com.omniversity.server.user.entity.University;

public record PublicUserProfileDto(
        String name,
        Date dateOfBirth,
        University university,
        String userId,
        String profilePicture
) implements ReturnDto {}
