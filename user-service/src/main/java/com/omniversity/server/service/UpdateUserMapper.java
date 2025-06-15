package com.omniversity.server.service;

import com.omniversity.server.user.dto.UpdateAccountDto;
import com.omniversity.server.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserMapper {
    public void updateEntity(User user, UpdateAccountDto updateAccountDto) {
        if (updateAccountDto.preferredLanguage() != null) {
            user.setPreferredLanguage(updateAccountDto.preferredLanguage());
        }
        if (updateAccountDto.profilePicture() != null) {
            user.setProfilePicture(updateAccountDto.profilePicture());
        }
        if (updateAccountDto.userId() != null) {
            user.setUserId(updateAccountDto.userId());
        }
        if (updateAccountDto.exchangeEnd() != null) {
            user.setExchangeEnd(updateAccountDto.exchangeEnd());
        }
    }
}
