package com.omniversity.server.service.Mapper.UserResponse;

import com.omniversity.server.user.dto.response.ExchangeUserResponseNoPasswordDto;
import com.omniversity.server.user.dto.response.FriendSuggestionDto;
import com.omniversity.server.user.dto.response.ProspectiveUserResponseNoPasswordDto;
import com.omniversity.server.user.dto.response.PublicUserProfileDto;
import com.omniversity.server.user.entity.User;

public interface UserResponseMapper {
    ExchangeUserResponseNoPasswordDto toResponseExchangeDto(User user);
    ProspectiveUserResponseNoPasswordDto toResponseProspectiveDto(User user);
    PublicUserProfileDto toPublicUserProfileDto(User user);
    FriendSuggestionDto toFriendSuggestionDto(User user);
}
