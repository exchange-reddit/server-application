package com.omniversity.server.user.AllUser.dto.request;

import com.omniversity.server.user.entity.Enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DeleteUserDto(

        @NotBlank(message = "User password must be provided to delete an account")
        String password,

        @NotBlank(message = "User ID must be provided to delete an account")
        long userId,

        @Email(message = "Email should be valid")
        @NotBlank(message = "Home email must be provided to delete an account")
        String homeEmail

) {
}
