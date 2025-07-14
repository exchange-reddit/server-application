package com.omniversity.server.user.ProspectiveUser.mapper.functional;

import com.omniversity.server.user.ProspectiveUser.dto.request.ProspectiveUserRegistrationDto;
import com.omniversity.server.user.entity.Enums.Country;
import com.omniversity.server.user.entity.Enums.Gender;
import com.omniversity.server.user.entity.Enums.Language;
import com.omniversity.server.user.entity.Enums.Program;
import com.omniversity.server.user.entity.Enums.University;
import com.omniversity.server.user.entity.ProspectiveUser;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProspectiveUserMapperTest {

    private final ProspectiveUserMapper mapper = Mappers.getMapper(ProspectiveUserMapper.class);

    @Test
    void testToEntity() {
        ProspectiveUserRegistrationDto dto = new ProspectiveUserRegistrationDto(
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
                University.KTH_ROYAL_INSTITUTE_OF_TECHNOLOGY
        );

        ProspectiveUser entity = mapper.toEntity(dto);

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
        assertEquals(dto.preferredLanguage(), entity.getPreferredLanguage());
    }
}