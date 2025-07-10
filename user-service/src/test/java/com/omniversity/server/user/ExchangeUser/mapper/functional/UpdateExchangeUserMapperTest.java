package com.omniversity.server.user.ExchangeUser.mapper.functional;

import com.omniversity.server.user.ExchangeUser.dto.request.UpdateExchangeUserDto;
import com.omniversity.server.user.entity.Enums.Language;
import com.omniversity.server.user.entity.ExchangeUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateExchangeUserMapperTest {

    private final UpdateExchangeUserMapper mapper = new UpdateExchangeUserMapper();

    @Test
    void testUpdateUserFromDto() {
        UpdateExchangeUserDto dto = new UpdateExchangeUserDto(
                "newUsername",
                null,
                Language.ENGLISH,
                null
        );

        ExchangeUser entity = new ExchangeUser();
        mapper.updateEntity(entity, dto);

        assertEquals(dto.userName(), entity.getUserName());
        assertEquals(dto.preferredLanguage(), entity.getPreferredLanguage());
    }
}