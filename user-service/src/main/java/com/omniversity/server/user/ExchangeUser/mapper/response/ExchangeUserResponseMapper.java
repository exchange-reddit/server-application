package com.omniversity.server.user.ExchangeUser.mapper.response;

import com.omniversity.server.user.ExchangeUser.dto.response.ExchangeUserResponseNoPasswordDto;
import com.omniversity.server.user.ExchangeUser.dto.response.FriendSuggestionDto;
import com.omniversity.server.user.ExchangeUser.dto.response.PublicUserProfileDto;
import com.omniversity.server.user.entity.ExchangeUser;

public interface ExchangeUserResponseMapper {
    ExchangeUserResponseNoPasswordDto toResponseExchangeDto(ExchangeUser exchangeUser);
    PublicUserProfileDto toPublicUserProfileDto(ExchangeUser exchangeUser);
    FriendSuggestionDto toFriendSuggestionDto(ExchangeUser exchangeUser);

}
