package com.omniversity.server.user.ExchangeUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omniversity.server.user.ExchangeUser.dto.request.RegistrationDto;
import com.omniversity.server.user.ExchangeUser.dto.response.PublicUserProfileDto;
import com.omniversity.server.user.ExchangeUser.dto.response.ReturnDto;
import com.omniversity.server.user.ExchangeUser.mapper.response.ExchangeUserResponse;
import com.omniversity.server.user.entity.Enums.*;
import com.omniversity.server.user.entity.ExchangeUser;
import com.omniversity.server.user.exception.NoSuchUserException;
import com.omniversity.server.user.exception.UserAlreadyExistsException;
import com.omniversity.server.user.exception.WeakPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ExchangeUserService exchangeUserService;

    @Mock
    private ExchangeUserResponse exchangeUserResponse;

    @InjectMocks
    private ExchangeUserController exchangeUserController;

    @Autowired
    private ObjectMapper objectMapper;

    private ExchangeUser user;

    @BeforeEach
    void setUp() {
        user = new ExchangeUser();
        user.setId(1L);
        user.setUserName("testUser");
        user.setPrivateEmail("test@example.com");
    }

    @Test
    void testGetUserProfile_Success() {
        PublicUserProfileDto expectedDto = new PublicUserProfileDto(
                "Test",
                "User",
                LocalDate.of(2000, 1, 1),
                University.KOREA_UNIVERSITY,
                "testUser",
                null
        );

        when(exchangeUserService.getPublicUserProfileId(1L, 1)).thenReturn(expectedDto);

        ResponseEntity response = exchangeUserController.getUserProfile(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, exchangeUserService.getPublicUserProfileId(1L, 1));
    }

    @Test
    void testGetUserProfile_Fail_NoSuchUser() {
        when(exchangeUserService.getPublicUserProfileId(2L, 1)).thenThrow(new NoSuchUserException("User with ID 2 not found"));

        ResponseEntity response = exchangeUserController.getUserProfile(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with ID 2 not found", response.getBody());
    }

    @Test
    void testGetUserProfile_Fail_InternalServerError() {
        when(exchangeUserService.getPublicUserProfileId(eq(3L), anyInt()))
                .thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> response = exchangeUserController.getUserProfile(3L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetUserProfileUserName_Success() {
        PublicUserProfileDto expectedDto = new PublicUserProfileDto(
                "Test",
                "User",
                LocalDate.of(2000, 1, 1),
                University.KOREA_UNIVERSITY,
                "testUser",
                null
        );


        when(exchangeUserService.getPublicUserProfileUserName("testUser")).thenReturn(expectedDto);

        ResponseEntity response = exchangeUserController.getUserProfileUserName("testUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, exchangeUserService.getPublicUserProfileUserName("testUser"));
    }

    @Test
    void testGetUserProfileUserName_Fail_NoSuchUser() {
        when(exchangeUserService.getPublicUserProfileUserName("nonexistentUser")).thenThrow(new NoSuchUserException("User with username nonexistentUser not found"));

        ResponseEntity response = exchangeUserController.getUserProfileUserName("nonexistentUser");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with username nonexistentUser not found", response.getBody());
    }

    @Test
    void testGetUserProfileUserName_Fail_InternalServerError() {
        when(exchangeUserService.getPublicUserProfileUserName("badRequest"))
                .thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> response = exchangeUserController.getUserProfileUserName("badRequest");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetRecommendationProfile_Success() {
        PublicUserProfileDto expectedDto = new PublicUserProfileDto(
                "Test",
                "User",
                LocalDate.of(2000, 1, 1),
                University.KOREA_UNIVERSITY,
                "testUser",
                null
        );

        when(exchangeUserService.getPublicUserProfileId(1L, 2)).thenReturn(expectedDto);

        ResponseEntity response = exchangeUserController.getRecommendationProfile(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());
    }

    @Test
    void testGetRecommendationProfile_Fail_NoSuchUser() {
        when(exchangeUserService.getPublicUserProfileId(2L, 2)).thenThrow(new NoSuchUserException("User with ID 2 not found"));

        ResponseEntity response = exchangeUserController.getRecommendationProfile(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with ID 2 not found", response.getBody());
    }

    @Test
    void testGetRecommendationProfile_Fail_InternalServerError() {
        when(exchangeUserService.getPublicUserProfileId(eq(3L), anyInt()))
                .thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> response = exchangeUserController.getRecommendationProfile(3L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testRegisterExchangeUser_Success() {
        RegistrationDto registrationDto = new RegistrationDto(
                "Test",
                "M",
                "User",
                Gender.MALE,
                LocalDate.of(2000, 1, 1),
                "test@example.com",
                University.KOREA_UNIVERSITY,
                "home@example.com",
                "password",
                "testUser",
                Country.UNITED_STATES,
                Language.ENGLISH,
                Program.COMPUTER_SCIENCE,
                University.KTH_ROYAL_INSTITUTE_OF_TECHNOLOGY,
                "exchange@example.com",
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2025, 5, 31)
        );

        ExchangeUser expectedUser = user;

        when(exchangeUserService.registerExchangeUser(registrationDto)).thenReturn(user);

        ResponseEntity response = exchangeUserController.registerExchangeUser(registrationDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());

    }

    @Test
    void testRegisterExchangeUser_Fail_UserAlreadyExists() {
        RegistrationDto registrationDto = new RegistrationDto(
                "Test",
                "M",
                "User",
                Gender.MALE,
                LocalDate.of(2000, 1, 1),
                "test@example.com",
                University.KOREA_UNIVERSITY,
                "home@example.com",
                "password",
                "testUser",
                Country.UNITED_STATES,
                Language.ENGLISH,
                Program.COMPUTER_SCIENCE,
                University.KTH_ROYAL_INSTITUTE_OF_TECHNOLOGY,
                "exchange@example.com",
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2025, 5, 31)
        );

        when(exchangeUserService.registerExchangeUser(registrationDto)).thenThrow(new UserAlreadyExistsException("User with username testUser already exists"));

        ResponseEntity response = exchangeUserController.registerExchangeUser(registrationDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User with username testUser already exists", response.getBody());
    }

    @Test
    void testRegisterExchangeUser_Fail_WeakPassword() {
        RegistrationDto registrationDto = new RegistrationDto(
                "Test",
                "M",
                "User",
                Gender.MALE,
                LocalDate.of(2000, 1, 1),
                "test@example.com",
                University.KOREA_UNIVERSITY,
                "home@example.com",
                "password",
                "testUser",
                Country.UNITED_STATES,
                Language.ENGLISH,
                Program.COMPUTER_SCIENCE,
                University.KTH_ROYAL_INSTITUTE_OF_TECHNOLOGY,
                "exchange@example.com",
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2025, 5, 31)
        );

        when(exchangeUserService.registerExchangeUser(registrationDto)).thenThrow(new WeakPasswordException("Password is too weak"));

        ResponseEntity response = exchangeUserController.registerExchangeUser(registrationDto);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("Password is too weak", response.getBody());
    }

    @Test
    void testRegisterExchangeUser_Fail_InternalServerError() {
        RegistrationDto registrationDto = new RegistrationDto(
                "Test",
                "M",
                "User",
                Gender.MALE,
                LocalDate.of(2000, 1, 1),
                "test@example.com",
                University.KOREA_UNIVERSITY,
                "home@example.com",
                "password",
                "testUser",
                Country.UNITED_STATES,
                Language.ENGLISH,
                Program.COMPUTER_SCIENCE,
                University.KTH_ROYAL_INSTITUTE_OF_TECHNOLOGY,
                "exchange@example.com",
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2025, 5, 31)
        );

        when(exchangeUserService.registerExchangeUser(registrationDto))
                .thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> response = exchangeUserController.registerExchangeUser(registrationDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}