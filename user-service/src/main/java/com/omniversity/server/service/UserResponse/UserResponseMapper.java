package com.omniversity.server.service.UserResponse;

import com.omniversity.server.user.dto.response.ExchangeUserResponseNoPasswordDto;
import com.omniversity.server.user.dto.response.ProspectiveUserResponseNoPasswordDto;
import com.omniversity.server.user.entity.User;

public interface UserResponseMapper {
    ExchangeUserResponseNoPasswordDto toResponseExchangeDto(User user);
    ProspectiveUserResponseNoPasswordDto toResponseProspectiveDto(User user);
}
