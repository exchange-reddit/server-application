package com.omniversity.server.user.ProspectiveUser.dto.request;

import com.omniversity.server.user.entity.Enums.*;
import jakarta.validation.constraints.*;


import java.time.LocalDate;

public record ProspectiveUserRegistrationDto(
        @NotBlank(message = "First name must be provided")
        String firstName,

        String middleName,

        @NotBlank(message = "Last name must be provided")
        String lastName,

        @NotEmpty(message = "Gender must be provided")
        Gender gender,

        @NotEmpty(message = "Date of Birth must be provide")
        LocalDate dateOfBirth,

        @Email(message = "Provide a valid form of email")
        String privateEmail,

        @NotBlank(message = "Home university must be provided")
        University homeUni,

        @NotBlank(message = "Home university's email must be provided")
        @Email(message = "Provide a valid form of email")
        String homeEmail,

        @NotBlank(message = "Password must be provided")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotBlank(message = "User name must be provided")
        String userName,

        @NotBlank(message = "Nationality must be provided")
        Country nationality,

        @NotEmpty(message = "Preferred language must be provide")
        Language preferredLanguage,

        @NotEmpty(message = "Your program must be provided")
        Program program,

        @NotEmpty(message = "Desired university of exchange must be set")
        University desiredUniversity

        ){}
