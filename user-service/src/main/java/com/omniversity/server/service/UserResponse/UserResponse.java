package com.omniversity.server.service.UserResponse;

import com.omniversity.server.user.dto.response.ExchangeUserResponseNoPasswordDto;

import com.omniversity.server.user.dto.response.ProspectiveUserResponseNoPasswordDto;
import com.omniversity.server.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserResponse implements UserResponseMapper{
    @Override
    public ExchangeUserResponseNoPasswordDto toResponseExchangeDto(User user) {
        ExchangeUserResponseNoPasswordDto dto = new ExchangeUserResponseNoPasswordDto(
                user.getName(),
                user.getPrivateEmail(),
                user.getUserId(),
                user.getDateOfBirth(),
                user.getExchangeStart(),
                user.getExchangeEnd(),
                user.isAdmin(),
                user.getHomeUni(),
                user.getExchangeUni(),
                user.getNationality(),
                user.getPreferredLanguage(),
                user.getProfilePicture()
                );

        return dto;
    }

    @Override
    public ProspectiveUserResponseNoPasswordDto toResponseProspectiveDto(User user) {
        ProspectiveUserResponseNoPasswordDto dto = new ProspectiveUserResponseNoPasswordDto(
                user.getName(),
                user.getPrivateEmail(),
                user.getUserId(),
                user.getDateOfBirth(),
                user.isAdmin(),
                user.getHomeUni(),
                user.getNationality(),
                user.getPreferredLanguage(),
                user.getProfilePicture()
        );

        return dto;
    }
}
