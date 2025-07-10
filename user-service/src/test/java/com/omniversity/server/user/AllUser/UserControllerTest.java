package com.omniversity.server.user.AllUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omniversity.server.log.LogService;
import com.omniversity.server.log.dto.accountDeleteLogDto;
import com.omniversity.server.log.dto.pwChangeLogDto;
import com.omniversity.server.service.PasswordValidator;
import com.omniversity.server.user.AllUser.dto.request.ChangeNationalityDto;
import com.omniversity.server.user.AllUser.dto.request.ChangePasswordDto;
import com.omniversity.server.user.AllUser.dto.request.DeleteUserDto;
import com.omniversity.server.user.entity.Enums.Country;
import com.omniversity.server.user.entity.ExchangeUser;
import com.omniversity.server.user.exception.ChangedPasswordSameException;
import com.omniversity.server.user.exception.NoSuchUserException;
import com.omniversity.server.user.exception.PassedGracePeriodException;
import com.omniversity.server.user.exception.WrongPasswordException;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private LogService logService;

    @Mock
    private AbstractUserRepository abstractUserRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private PasswordValidator passwordValidator;

    @InjectMocks
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    private ExchangeUser user;

    private pwChangeLogDto pwChangeLogDto;

    @BeforeEach
    void setUp() {
        user = new ExchangeUser();
        user.setId(1L);
        user.setUserName("testUser");
        user.setPrivateEmail("test@example.com");
    }

    @Test
    void testCheckUserIdTaken_True() {
        when(userService.checkUserNameTaken("testUser")).thenReturn(true);

        ResponseEntity<Boolean> response = userController.checkUserIdTaken("testUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testCheckUserIdTaken_False() {
        when(userService.checkUserNameTaken("testUser")).thenReturn(false);

        ResponseEntity<Boolean> response = userController.checkUserIdTaken("testUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void testCheckUserIdTaken_Fail_InternalServerError() {
        when(userService.checkUserNameTaken("badRequest"))
                .thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> response = userController.checkUserIdTaken("badRequest");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    void testGetUser_Success() {
        when(userService.getUser(1L)).thenReturn(user);

        ResponseEntity response = userController.getUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetUser_Fail_NoSuchUser() {
        when(userService.getUser(1L)).thenThrow(new NoSuchUserException("The following user was not found"));

        ResponseEntity response = userController.getUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetUser_Fail_InternalServerError() {
        when(userService.getUser(1L))
                .thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> response = userController.getUser(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testChangePw_Success() {
        ChangePasswordDto dto = new ChangePasswordDto(
                "testPassword",
                "StrongPassword123@"
        );

        ResponseEntity response = userController.changePW(1L, dto, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testChangePw_Fail_Forbidden() {
        ChangePasswordDto dto = new ChangePasswordDto(
                "wrongPassword",
                "StrongPassword123@"
        );

        when(userService.changePassword(dto, 1L)).thenThrow(new WrongPasswordException("Provided password is incorrect"));

        ResponseEntity response = userController.changePW(1L, dto, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Provided password is incorrect", response.getBody());

    }

    @Test
    void testChangePw_Fail_SamePassword() {
        ChangePasswordDto dto = new ChangePasswordDto(
                "currentPassword",
                "currentPassword"
        );

        when(userService.changePassword(dto, 1L)).thenThrow(new ChangedPasswordSameException("New password cannot be the same as the old password."));

        ResponseEntity response = userController.changePW(1L, dto, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("New password cannot be the same as the old password.", response.getBody());
    }

    @Test
    void testChangePw_Fail_WeakPassword() {
        ChangePasswordDto dto = new ChangePasswordDto(
                "currentPassword",
                "weak"
        );

        when(userService.changePassword(dto, 1L)).thenThrow(new RuntimeException("Password must be secure"));

        ResponseEntity response = userController.changePW(1L, dto, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password must be secure", response.getBody());
    }

    @Test
    void testChangePw_Fail_NoSuchUser() {
        ChangePasswordDto dto = new ChangePasswordDto(
                "currentPassword",
                "newPassword"
        );

        when(userService.changePassword(dto, 2L)).thenThrow(new NoSuchUserException("User ID 2 does not exist in our system."));

        ResponseEntity response = userController.changePW(2L, dto, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User ID 2 does not exist in our system.", response.getBody());
    }

    @Test
    void testChangeNationality_Success() {
        ChangeNationalityDto dto = new ChangeNationalityDto(
                Country.SWEDEN
        );

        ResponseEntity response = userController.changeNationality(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testChangeNationality_Fail_NoSuchUser() {
        ChangeNationalityDto dto = new ChangeNationalityDto(
                Country.SWEDEN
        );

        doThrow(new NoSuchUserException("User ID 1 does not exist in our system")).when(userService).changeNationality(1L, dto);

        ResponseEntity response = userController.changeNationality(1L, dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User ID 1 does not exist in our system", response.getBody());
    }

    @Test
    void testChangeNationality_Fail_PassedGracePeriod() {
        ChangeNationalityDto dto = new ChangeNationalityDto(
                Country.SWEDEN
        );
        String exceptionMessage = "N days have passed since your registration to our service. The grace period to change your nationality is 2 weeks";

        doThrow(new PassedGracePeriodException(exceptionMessage)).when(userService).changeNationality(1L, dto);

        ResponseEntity response = userController.changeNationality(1L, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(exceptionMessage, response.getBody());
    }

    @Test
    void testDeleteUser_Success() {
        DeleteUserDto dto = new DeleteUserDto(
                "StrongPassword123@",
                1,
                "test@test.com"
        );

        accountDeleteLogDto returnDto = new accountDeleteLogDto(
                "1",
                "Delete account"
        );

        when(userService.deleteUser(dto)).thenReturn(returnDto);

        ResponseEntity response = userController.deleteUserAccount(dto, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteUser_Fail_WrongPassword() {
        DeleteUserDto dto = new DeleteUserDto(
                "wrongPassword",
                1,
                "test@test.com"
        );

        when(userService.deleteUser(dto)).thenThrow(new WrongPasswordException("The provided previous password does not match."));

        ResponseEntity response = userController.deleteUserAccount(dto, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("The provided previous password does not match.", response.getBody());
    }

    @Test
    void testDeleteUser_Fail_NoSuchUser() {
        DeleteUserDto dto = new DeleteUserDto(
                "password",
                2,
                "test@test.com"
        );

        when(userService.deleteUser(dto)).thenThrow(new NoSuchUserException("User ID 2 does not exist in our system."));

        ResponseEntity response = userController.deleteUserAccount(dto, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User ID 2 does not exist in our system.", response.getBody());
    }
}