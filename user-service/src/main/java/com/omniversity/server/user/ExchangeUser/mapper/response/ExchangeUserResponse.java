package com.omniversity.server.user.ExchangeUser.mapper.response;

import com.omniversity.server.user.ExchangeUser.dto.response.ExchangeUserResponseNoPasswordDto;
import com.omniversity.server.user.ExchangeUser.dto.response.FriendSuggestionDto;
import com.omniversity.server.user.ExchangeUser.dto.response.PublicUserProfileDto;
import com.omniversity.server.user.entity.ExchangeUser;
import org.springframework.stereotype.Component;

@Component
public class ExchangeUserResponse implements ExchangeUserResponseMapper {
    @Override
    public ExchangeUserResponseNoPasswordDto toResponseExchangeDto(ExchangeUser exchangeUser) {
        ExchangeUserResponseNoPasswordDto dto = new ExchangeUserResponseNoPasswordDto(
                exchangeUser.getFirstName(),
                exchangeUser.getLastName(),
                exchangeUser.getPrivateEmail(),
                exchangeUser.getUserName(),
                exchangeUser.getDateOfBirth(),
                exchangeUser.getExchangeStart(),
                exchangeUser.getExchangeEnd(),
                exchangeUser.getHomeUni(),
                exchangeUser.getExchangeUni(),
                exchangeUser.getProgram(),
                exchangeUser.getNationality(),
                exchangeUser.getPreferredLanguage(),
                exchangeUser.getProfilePicture()
        );

        return dto;
    }

    @Override
    public PublicUserProfileDto toPublicUserProfileDto(ExchangeUser exchangeUser) {
        PublicUserProfileDto dto = new PublicUserProfileDto(
                exchangeUser.getFirstName(),
                exchangeUser.getLastName(),
                exchangeUser.getDateOfBirth(),
                exchangeUser.getHomeUni(),
                exchangeUser.getUserName(),
                exchangeUser.getProfilePicture()
        );

        return dto;
    }

    @Override
    public FriendSuggestionDto toFriendSuggestionDto(ExchangeUser exchangeUser) {
        FriendSuggestionDto dto = new FriendSuggestionDto(
                exchangeUser.getFirstName(),
                exchangeUser.getLastName(),
                exchangeUser.getProfilePicture(),
                exchangeUser.getHomeUni(),
                exchangeUser.getProgram()
        );

        return dto;
    }
}