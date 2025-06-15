package com.omniversity.server.user.dto;

import com.omniversity.server.user.entity.University;
import com.omniversity.server.user.entity.UserType;
import jakarta.validation.constraints.*;


import java.util.Date;

public record ProspectiveUserRegistrationDto(
        UserType userType,

        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Private email must be provided")
        @Email(message = "Email should be valid")
        String privateEmail,

        @NotBlank(message = "You must provide your home university email")
        @Email(message = "Email should be valid")
        String homeEmail,

        @NotBlank(message = "You must provide your home university")
        University homeUni,

        @NotBlank(message = "Password must be provided")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotBlank(message = "User ID must be provided")
        String userId,

        @NotBlank(message = "Your date of birth must be provided")
        Date dateOfBirth,

        String profilePicture,

        Boolean isAdmin
        ){}
