package com.omniversity.server.user.entity;

import com.omniversity.server.user.entity.Enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExchangeUserTest {
    private ExchangeUser user;

    @BeforeEach
    void setUp() {
        user = new ExchangeUser();
        user.setId(1L);
        user.setGender(Gender.MALE);
        user.setFirstName("Test");
        user.setMiddleName("User");
        user.setLastName("One");
        user.setDateOfBirth(LocalDate.of(2003, 5, 21));
        user.setPrivateEmail("test@private.com");
        user.setPrivateEmailVerified(true);
        user.setHomeUni(University.KTH_ROYAL_INSTITUTE_OF_TECHNOLOGY);
        user.setHomeEmail("test@home.com");
        user.setHomeEmailVerified(true);
        user.setPasswordHash("hashedPassword");
        user.setUserName("testUser");
        user.setNationality(Country.REPUBLIC_OF_KOREA);
        user.setPreferredLanguage(Language.ENGLISH);
        user.setProfilePicture("randomUrl");
        user.setProgram(Program.COMPUTER_SCIENCE);
        user.setIsActive(true);
        user.setRegistrationDate(LocalDate.now());
    }

    @Test
    void testGetId() {
        Long expectedUserId = 1L;
        assertEquals(expectedUserId, user.getId());
    }

    @Test
    void testSetAndGetGender() {
        Gender expectedGender = Gender.FEMALE;
        user.setGender(expectedGender);
        assertEquals(expectedGender, user.getGender());
    }

    @Test
    void testSetAndGetFirstName() {
        String expectedFirstName = "NewFirstName";
        user.setFirstName(expectedFirstName);
        assertEquals(expectedFirstName, user.getFirstName());
    }

    @Test
    void testSetAndGetMiddleName() {
        String expectedMiddleName = "NewMiddleName";
        user.setMiddleName(expectedMiddleName);
        assertEquals(expectedMiddleName, user.getMiddleName());
    }

    @Test
    void testSetAndGetLastName() {
        String expectedLastName = "NewLastName";
        user.setLastName(expectedLastName);
        assertEquals(expectedLastName, user.getLastName());
    }

    @Test
    void testSetAndGetDateOfBirth() {
        LocalDate expectedDateOfBirth = LocalDate.of(1990, 1, 1);
        user.setDateOfBirth(expectedDateOfBirth);
        assertEquals(expectedDateOfBirth, user.getDateOfBirth());
    }

    @Test
    void testSetAndGetPrivateEmail() {
        String expectedPrivateEmail = "newprivate@example.com";
        user.setPrivateEmail(expectedPrivateEmail);
        assertEquals(expectedPrivateEmail, user.getPrivateEmail());
    }

    @Test
    void testSetAndGetHomeUni() {
        University expectedHomeUni = University.KTH_ROYAL_INSTITUTE_OF_TECHNOLOGY;
        user.setHomeUni(expectedHomeUni);
        assertEquals(expectedHomeUni, user.getHomeUni());
    }

    @Test
    void testSetAndGetHomeEmail() {
        String expectedHomeEmail = "newhome@example.com";
        user.setHomeEmail(expectedHomeEmail);
        assertEquals(expectedHomeEmail, user.getHomeEmail());
    }

    @Test
    void testSetAndGetPasswordHash() {
        String expectedPasswordHash = "newHashedPassword";
        user.setPasswordHash(expectedPasswordHash);
        assertEquals(expectedPasswordHash, user.getPasswordHash());
    }

    @Test
    void testSetAndGetUserName() {
        String expectedUserName = "newUserName";
        user.setUserName(expectedUserName);
        assertEquals(expectedUserName, user.getUserName());
    }

    @Test
    void testSetAndGetNationality() {
        Country expectedNationality = Country.REPUBLIC_OF_KOREA;
        user.setNationality(expectedNationality);
        assertEquals(expectedNationality, user.getNationality());
    }

    @Test
    void testSetAndGetPreferredLanguage() {
        Language expectedLanguage = Language.ENGLISH;
        user.setPreferredLanguage(expectedLanguage);
        assertEquals(expectedLanguage, user.getPreferredLanguage());
    }

    @Test
    void testSetAndGetProfilePicture() {
        String expectedProfilePicture = "newProfilePicUrl";
        user.setProfilePicture(expectedProfilePicture);
        assertEquals(expectedProfilePicture, user.getProfilePicture());
    }

    @Test
    void testSetAndGetProgram() {
        Program expectedProgram = Program.MANAGEMENT;
        user.setProgram(expectedProgram);
        assertEquals(expectedProgram, user.getProgram());
    }

    @Test
    void testSetAndGetIsActive() {
        boolean expectedIsActive = false;
        user.setIsActive(expectedIsActive);
        assertEquals(expectedIsActive, user.getIsActive());
    }

    @Test
    void testSetAndGetRegistrationDate() {
        LocalDate expectedRegistrationDate = LocalDate.of(2023, 1, 1);
        user.setRegistrationDate(expectedRegistrationDate);
        assertEquals(expectedRegistrationDate, user.getRegistrationDate());
    }

    @Test
    void testSetAndGetExchangeUni() {
        University expectedUniversity = University.KOREA_UNIVERSITY;
        user.setExchangeUni(expectedUniversity);
        assertEquals(expectedUniversity, user.getExchangeUni());
    }

    @Test
    void testSetAndGetExchangeEmail() {
        String expectedEmail = "test@exchange.com";
        user.setExchangeEmail(expectedEmail);
        assertEquals(expectedEmail, user.getExchangeEmail());
    }

    @Test
    void testSetAndGetExchangeEmailVerified() {
        boolean expectedVerified = true;
        user.setExchangeEmailVerified(expectedVerified);
        assertEquals(expectedVerified, user.getExchangeEmailVerified());
    }

    @Test
    void testSetAndGetExchangeStart() {
        LocalDate expectedDate = LocalDate.of(2024, 8, 1);
        user.setExchangeStart(expectedDate);
        assertEquals(expectedDate, user.getExchangeStart());
    }

    @Test
    void testSetAndGetExchangeEnd() {
        LocalDate expectedDate = LocalDate.of(2024, 12, 31);
        user.setExchangeEnd(expectedDate);
        assertEquals(expectedDate, user.getExchangeEnd());
    }
}
