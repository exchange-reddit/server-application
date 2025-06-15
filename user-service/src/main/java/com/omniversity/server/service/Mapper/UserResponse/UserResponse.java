package com.omniversity.server.service.Mapper.UserResponse;

import com.omniversity.server.user.dto.response.ExchangeUserResponseNoPasswordDto;

import com.omniversity.server.user.dto.response.FriendSuggestionDto;
import com.omniversity.server.user.dto.response.ProspectiveUserResponseNoPasswordDto;
import com.omniversity.server.user.dto.response.PublicUserProfileDto;
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
                user.getProgram(),
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
                user.getProgram(),
                user.getPreferredLanguage(),
                user.getProfilePicture()
        );

        return dto;
    }

    @Override
    public PublicUserProfileDto toPublicUserProfileDto(User user) {
        PublicUserProfileDto dto = new PublicUserProfileDto(
                user.getName(),
                user.getDateOfBirth(),
                user.getHomeUni(),
                user.getUserId(),
                user.getProfilePicture()
        );

        return dto;
    }

    @Override
    public FriendSuggestionDto toFriendSuggestionDto(User user) {
        FriendSuggestionDto dto = new FriendSuggestionDto(
                user.getName(),
                user.getProfilePicture(),
                user.getHomeUni(),
                user.getProgram()
        );

        return dto;
    }

}
