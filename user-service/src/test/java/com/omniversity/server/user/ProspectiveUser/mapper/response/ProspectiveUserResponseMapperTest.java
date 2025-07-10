package com.omniversity.server.user.ProspectiveUser.mapper.response;

import com.omniversity.server.user.ProspectiveUser.dto.response.ProspectiveUserResponseNoPasswordDto;
import com.omniversity.server.user.entity.Enums.Country;

import com.omniversity.server.user.entity.Enums.Language;
import com.omniversity.server.user.entity.Enums.Program;
import com.omniversity.server.user.entity.Enums.University;
import com.omniversity.server.user.entity.ProspectiveUser;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProspectiveUserResponseMapperTest {

    private final ProspectiveUserResponseMapper mapper = Mappers.getMapper(ProspectiveUserResponseMapper.class);

    @Test
    void testToProspectiveUserResponseNoPasswordDto() {
        ProspectiveUser entity = new ProspectiveUser();
        entity.setFirstName("Test");
        entity.setLastName("User");
        entity.setPrivateEmail("test@example.com");
        entity.setUserName("testUser");
        entity.setDateOfBirth(LocalDate.of(2000, 1, 1));
        entity.setHomeUni(University.KOREA_UNIVERSITY);
        entity.setProgram(Program.COMPUTER_SCIENCE);
        entity.setNationality(Country.UNITED_STATES);
        entity.setPreferredLanguage(Language.ENGLISH);

        ProspectiveUserResponseNoPasswordDto dto = mapper.toResponseProspectiveDto(entity);

        assertEquals(entity.getFirstName(), dto.firstName());
        assertEquals(entity.getLastName(), dto.lastName());
        assertEquals(entity.getPrivateEmail(), dto.privateEmail());
        assertEquals(entity.getUserName(), dto.userName());
        assertEquals(entity.getDateOfBirth(), dto.dateOfBirth());
        //assertEquals(entity.getHomeUni(), dto.homeUniversity());
        assertEquals(entity.getProgram(), dto.program());
        assertEquals(entity.getNationality(), dto.nationality());
        assertEquals(entity.getPreferredLanguage(), dto.preferredLanguage());
    }
}