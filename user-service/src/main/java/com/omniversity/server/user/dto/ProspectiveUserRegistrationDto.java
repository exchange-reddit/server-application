package com.omniversity.server.user.dto;

import com.omniversity.server.user.entity.*;
import jakarta.validation.constraints.*;


import java.util.Date;

public record ProspectiveUserRegistrationDto(
        UserType userType,

        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Gender cannot be blank")
        Gender gender,

        @NotBlank(message = "Private email must be provided")
        @Email(message = "Email should be valid")
        String privateEmail,

        @NotBlank(message = "You must provide your home university email")
        @Email(message = "Email should be valid")
        String homeEmail,

        @NotBlank(message = "You must provide your home university")
        University homeUni,

        Country nationality,

        Language preferredLanguage,

        @NotBlank(message = "Password must be provided")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotBlank(message = "User ID must be provided")
        String userId,

        @NotBlank(message = "Your date of birth must be provided")
        Date dateOfBirth,

        @NotBlank(message = "Your program must be provided")
        Program program,

        String profilePicture

        ){}
