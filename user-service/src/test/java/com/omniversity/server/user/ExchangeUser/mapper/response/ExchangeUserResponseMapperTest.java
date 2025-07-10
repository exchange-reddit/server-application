package com.omniversity.server.user.ExchangeUser.mapper.response;

import com.omniversity.server.user.ExchangeUser.dto.response.ExchangeUserResponseNoPasswordDto;
import com.omniversity.server.user.ExchangeUser.dto.response.FriendSuggestionDto;
import com.omniversity.server.user.ExchangeUser.dto.response.PublicUserProfileDto;
import com.omniversity.server.user.entity.Enums.Country;

import com.omniversity.server.user.entity.Enums.Language;
import com.omniversity.server.user.entity.Enums.Program;
import com.omniversity.server.user.entity.Enums.University;
import com.omniversity.server.user.entity.ExchangeUser;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExchangeUserResponseMapperTest {

    private final ExchangeUserResponseMapper mapper = Mappers.getMapper(ExchangeUserResponseMapper.class);

    @Test
    void testToExchangeUserResponseNoPasswordDto() {
        ExchangeUser entity = new ExchangeUser();
        entity.setFirstName("Test");
        entity.setLastName("User");
        entity.setPrivateEmail("test@example.com");
        entity.setUserName("testUser");
        entity.setDateOfBirth(LocalDate.of(2000, 1, 1));
        entity.setExchangeStart(LocalDate.of(2024, 9, 1));
        entity.setExchangeEnd(LocalDate.of(2025, 5, 31));
        entity.setHomeUni(University.KOREA_UNIVERSITY);
        entity.setExchangeUni(University.KTH_ROYAL_INSTITUTE_OF_TECHNOLOGY);
        entity.setProgram(Program.COMPUTER_SCIENCE);
        entity.setNationality(Country.UNITED_STATES);
        entity.setPreferredLanguage(Language.ENGLISH);

        ExchangeUserResponseNoPasswordDto dto = mapper.toResponseExchangeDto(entity);

        assertEquals(entity.getFirstName(), dto.firstName());
        assertEquals(entity.getLastName(), dto.lastName());
        assertEquals(entity.getPrivateEmail(), dto.privateEmail());
        assertEquals(entity.getUserName(), dto.userName());
        assertEquals(entity.getDateOfBirth(), dto.dateOfBirth());
        assertEquals(entity.getExchangeStart(), dto.exchangeStart());
        assertEquals(entity.getExchangeEnd(), dto.exchangeEnd());
        //assertEquals(entity.getHomeUni(), dto.homeUniversity());
        //assertEquals(entity.getExchangeUni(), dto.exchangeUniversity());
        assertEquals(entity.getProgram(), dto.program());
        assertEquals(entity.getNationality(), dto.nationality());
        assertEquals(entity.getPreferredLanguage(), dto.preferredLanguage());
    }

    @Test
    void testToPublicUserProfileDto() {
        ExchangeUser entity = new ExchangeUser();
        entity.setFirstName("Test");
        entity.setLastName("User");
        entity.setDateOfBirth(LocalDate.of(2000, 1, 1));
        entity.setUserName("testUser");
        entity.setProfilePicture("randomUrl");

        PublicUserProfileDto dto = mapper.toPublicUserProfileDto(entity);

        assertEquals(entity.getFirstName(), dto.firstName());
        assertEquals(entity.getLastName(), dto.lastName());
        assertEquals(entity.getDateOfBirth(), dto.dateOfBirth());
        assertEquals(entity.getUserName(), dto.userName());
        assertEquals(entity.getProfilePicture(), dto.profilePicture());
    }

    @Test
    void testToFriendSuggestionDto() {
        ExchangeUser entity = new ExchangeUser();
        entity.setFirstName("Test");
        entity.setLastName("User");
        entity.setProfilePicture("randomUrl");
        entity.setProgram(Program.COMPUTER_SCIENCE);

        FriendSuggestionDto dto = mapper.toFriendSuggestionDto(entity);

        assertEquals(entity.getFirstName(), dto.firstName());
        assertEquals(entity.getLastName(), dto.lastName());
        assertEquals(entity.getProfilePicture(), dto.profilePicture());
        assertEquals(entity.getProgram(), dto.program());
    }
}