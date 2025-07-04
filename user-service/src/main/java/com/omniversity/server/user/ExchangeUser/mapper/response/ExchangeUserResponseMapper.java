package com.omniversity.server.user.ExchangeUser.mapper.response;

import com.omniversity.server.user.ExchangeUser.dto.response.ExchangeUserResponseNoPasswordDto;
import com.omniversity.server.user.ExchangeUser.dto.response.FriendSuggestionDto;
import com.omniversity.server.user.ExchangeUser.dto.response.PublicUserProfileDto;
import com.omniversity.server.user.entity.ExchangeUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExchangeUserResponseMapper {
    ExchangeUserResponseNoPasswordDto toResponseExchangeDto(ExchangeUser exchangeUser);
    PublicUserProfileDto toPublicUserProfileDto(ExchangeUser exchangeUser);
    FriendSuggestionDto toFriendSuggestionDto(ExchangeUser exchangeUser);

}
