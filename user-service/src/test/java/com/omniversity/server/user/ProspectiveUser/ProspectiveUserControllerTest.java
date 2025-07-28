package com.omniversity.server.user.ProspectiveUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omniversity.server.user.ProspectiveUser.dto.request.DesiredUniversityUpdateDto;
import com.omniversity.server.user.ProspectiveUser.dto.request.ProspectiveUserRegistrationDto;
import com.omniversity.server.user.ProspectiveUser.dto.response.ProspectiveUserResponseNoPasswordDto;
import com.omniversity.server.user.entity.Enums.*;
import com.omniversity.server.user.entity.ExchangeUser;
import com.omniversity.server.user.entity.ProspectiveUser;
import com.omniversity.server.user.exception.HashValidationFailedException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProspectiveUserControllerTest {
    @Mock
    private ProspectiveUserService prospectiveUserService;

    @InjectMocks
    private ProspectiveUserController prospectiveUserController;

    private ProspectiveUser user;

    @BeforeEach
    void setUp() {
        user = new ProspectiveUser();
        user.setId(1L);
        user.setUserName("testUser");
        user.setPrivateEmail("test@example.com");
    }

    @Test
    void testRegisterProspectiveUser_Success() {
        ProspectiveUserRegistrationDto registrationDto = new ProspectiveUserRegistrationDto(
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
        ProspectiveUserResponseNoPasswordDto expectedDto = new ProspectiveUserResponseNoPasswordDto(
                "Test",
                "User",
                "test@example.com",
                "testUser",
                LocalDate.of(2000, 1, 1),
                University.KOREA_UNIVERSITY,
                University.STOCKHOLM_UNIVERSITY,
                Program.COMPUTER_SCIENCE,
                Country.UNITED_STATES,
                Language.ENGLISH,
                null
        );

        ResponseEntity response = prospectiveUserController.registerProspectiveUser(registrationDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testRegisterProspectiveUser_Fail_UserAlreadyExists_Username() throws HashValidationFailedException {
        ProspectiveUserRegistrationDto registrationDto = new ProspectiveUserRegistrationDto(
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

        when(prospectiveUserService.registerProspectiveUser(registrationDto)).thenThrow(new UserAlreadyExistsException("User with username testUser already exists"));

        ResponseEntity response = prospectiveUserController.registerProspectiveUser(registrationDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User with username testUser already exists", response.getBody());
    }

    @Test
    void testRegisterProspectiveUser_Fail_UserAlreadyExists_HomeEmail() throws HashValidationFailedException {
        ProspectiveUserRegistrationDto registrationDto = new ProspectiveUserRegistrationDto(
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

        when(prospectiveUserService.registerProspectiveUser(registrationDto)).thenThrow(new UserAlreadyExistsException("You have already created an account using this email"));

        ResponseEntity response = prospectiveUserController.registerProspectiveUser(registrationDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("You have already created an account using this email", response.getBody());
    }

    @Test
    void testRegisterProspectiveUser_Fail_WeakPassword() throws HashValidationFailedException {
        ProspectiveUserRegistrationDto registrationDto = new ProspectiveUserRegistrationDto(
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

        when(prospectiveUserService.registerProspectiveUser(registrationDto)).thenThrow(new WeakPasswordException("Password is too weak"));

        ResponseEntity response = prospectiveUserController.registerProspectiveUser(registrationDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password is too weak", response.getBody());
    }

    @Test
    void testGetUserProfile_Fail_InternalServerError() throws HashValidationFailedException {
        ProspectiveUserRegistrationDto registrationDto = new ProspectiveUserRegistrationDto(
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

        when(prospectiveUserService.registerProspectiveUser(registrationDto))
                .thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> response = prospectiveUserController.registerProspectiveUser(registrationDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testUpdateDesiredUniversity_Success() {
        DesiredUniversityUpdateDto dto = new DesiredUniversityUpdateDto(University.STOCKHOLM_UNIVERSITY);
        ProspectiveUserResponseNoPasswordDto expectedDto = new ProspectiveUserResponseNoPasswordDto(
                "Test",
                "User",
                "test@example.com",
                "testUser",
                LocalDate.of(2000, 1, 1),
                University.KOREA_UNIVERSITY,
                University.STOCKHOLM_UNIVERSITY,
                Program.COMPUTER_SCIENCE,
                Country.UNITED_STATES,
                Language.ENGLISH,
                null
        );

        when(prospectiveUserService.updateDesiredExchangeUniversity(dto, 1L)).thenReturn(expectedDto);

        ResponseEntity response = prospectiveUserController.updateDesiredUniversity(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());
    }

    @Test
    void testUpdateDesiredUniversity_Fail_NoSuchUser() {
        DesiredUniversityUpdateDto dto = new DesiredUniversityUpdateDto(University.STOCKHOLM_UNIVERSITY);

        when(prospectiveUserService.updateDesiredExchangeUniversity(dto, 2L)).thenThrow(new NoSuchUserException("User with ID 2 not found"));

        ResponseEntity response = prospectiveUserController.updateDesiredUniversity(2L, dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with ID 2 not found", response.getBody());
    }

    @Test
    void testUpdateDesiredUniversity_Fail_InternalServerError() {
        DesiredUniversityUpdateDto dto = new DesiredUniversityUpdateDto(University.STOCKHOLM_UNIVERSITY);
        when(prospectiveUserService.updateDesiredExchangeUniversity(dto, 3L))
                .thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> response = prospectiveUserController.updateDesiredUniversity(3L, dto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}