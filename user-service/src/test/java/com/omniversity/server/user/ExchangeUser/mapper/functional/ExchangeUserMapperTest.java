package com.omniversity.server.user.ExchangeUser.mapper.functional;

import com.omniversity.server.user.ExchangeUser.dto.request.RegistrationDto;
import com.omniversity.server.user.entity.ExchangeUser;
import com.omniversity.server.user.entity.Enums.Country;
import com.omniversity.server.user.entity.Enums.Gender;
import com.omniversity.server.user.entity.Enums.Language;
import com.omniversity.server.user.entity.Enums.Program;
import com.omniversity.server.user.entity.Enums.University;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExchangeUserMapperTest {

    private final ExchangeUserMapper mapper = Mappers.getMapper(ExchangeUserMapper.class);

    @Test
    void testToEntity() {
        RegistrationDto dto = new RegistrationDto(
                "Test",
                "M",
                "User",
                Gender.MALE,
                LocalDate.of(2000, 1, 1),
                "test@example.com",
                University.KOREA_UNIVERSITY,
                "home@example.com",
                "homeEmailHash",
                "password",
                "testUser",
                Country.UNITED_STATES,
                Language.ENGLISH,
                Program.COMPUTER_SCIENCE,
                University.KTH_ROYAL_INSTITUTE_OF_TECHNOLOGY,
                "exchange@example.com",
                "exchangeEmailHash",
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2025, 5, 31)
        );
        ExchangeUser entity = mapper.toEntity(dto);

        assertEquals(dto.userName(), entity.getUserName());
        assertEquals(dto.firstName(), entity.getFirstName());
        assertEquals(dto.lastName(), entity.getLastName());
        assertEquals(dto.dateOfBirth(), entity.getDateOfBirth());
        assertEquals(dto.gender(), entity.getGender());
        assertEquals(dto.privateEmail(), entity.getPrivateEmail());
        assertEquals(dto.homeEmail(), entity.getHomeEmail());
        assertEquals(dto.nationality(), entity.getNationality());
        assertEquals(dto.homeUni(), entity.getHomeUni());
        assertEquals(dto.program(), entity.getProgram());
        assertEquals(dto.exchangeStart(), entity.getExchangeStart());
        assertEquals(dto.exchangeEnd(), entity.getExchangeEnd());
        assertEquals(dto.preferredLanguage(), entity.getPreferredLanguage());
    }
}