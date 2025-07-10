package com.omniversity.server.user.ExchangeUser.mapper.functional;

import com.omniversity.server.user.ExchangeUser.dto.request.UpdateExchangeUserDto;
import com.omniversity.server.user.ExchangeUser.dto.response.ExchangeUserResponseNoPasswordDto;
import com.omniversity.server.user.entity.ExchangeUser;
import org.springframework.stereotype.Component;

@Component
public class UpdateExchangeUserMapper {
    public void updateEntity(ExchangeUser user, UpdateExchangeUserDto dto) {
        if (dto.preferredLanguage() != null) {
            user.setPreferredLanguage(dto.preferredLanguage());
        }
        if (dto.profilePicture() != null) {
            user.setProfilePicture(dto.profilePicture());
        }
        if (dto.userName() != null) {
            user.setUserName(dto.userName());
        }
        if (dto.exchangeEnd() != null) {
            user.setExchangeEnd(dto.exchangeEnd());
        }
    }

    public ExchangeUserResponseNoPasswordDto toResponseExchangeDto(ExchangeUser user) {
        ExchangeUserResponseNoPasswordDto dto = new ExchangeUserResponseNoPasswordDto(
                user.getFirstName(),
                user.getLastName(),
                user.getPrivateEmail(),
                user.getUserName(),
                user.getDateOfBirth(),
                user.getExchangeStart(),
                user.getExchangeEnd(),
                user.getHomeUni(),
                user.getExchangeUni(),
                user.getProgram(),
                user.getNationality(),
                user.getPreferredLanguage(),
                user.getProfilePicture()
        );

        return dto;
    }
}
