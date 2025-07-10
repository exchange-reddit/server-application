package com.omniversity.server.user.AllUser;

import com.omniversity.server.log.dto.accountDeleteLogDto;
import com.omniversity.server.log.dto.pwChangeLogDto;
import com.omniversity.server.service.PasswordValidator;
import com.omniversity.server.user.AllUser.dto.request.ChangeNationalityDto;
import com.omniversity.server.user.AllUser.dto.request.ChangePasswordDto;
import com.omniversity.server.user.AllUser.dto.request.DeleteUserDto;
import com.omniversity.server.user.ExchangeUser.ExchangeUserService;
import com.omniversity.server.user.ExchangeUser.ExchangeUserRepository;
import com.omniversity.server.user.ExchangeUser.dto.request.RegistrationDto;
import com.omniversity.server.user.entity.AbstractUser;
import com.omniversity.server.user.entity.Enums.*;
import com.omniversity.server.user.entity.ExchangeUser;
import com.omniversity.server.user.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AbstractUserRepository abstractUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private ExchangeUserRepository exchangeUserRepository;

    @InjectMocks
    private UserService userService;

    private ExchangeUser user;
    private AbstractUser abstractUser;

    @BeforeEach
    void setUp() {
        user = new ExchangeUser();
        user.setId(1L);
        user.setUserName("testUser");
        user.setPasswordHash("encodedPassword");
        user.setRegistrationDate(LocalDate.now());
    }

    @Test
    void testCheckUserNameTaken_True() {
        when(abstractUserRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        assertTrue(userService.checkUserNameTaken("testUser"));
    }

    @Test
    void testCheckPrivateEmailRegistered_True() {
        when(abstractUserRepository.findByPrivateEmail("test@private.com")).thenReturn(Optional.empty());
        assertFalse(userService.checkEmailRegistered("test@private.com", 1));
    }

    @Test
    void testCheckPrivateEmailRegistered_False() {
        when(abstractUserRepository.findByPrivateEmail("test@private.com")).thenReturn(Optional.of(user));
        assertTrue(userService.checkEmailRegistered("test@private.com", 1));
    }

    @Test
    void testCheckExchangeEmailRegistered_True() {
        lenient().when(exchangeUserRepository.findByPrivateEmail("test@exchange.com")).thenReturn(Optional.empty());
        assertFalse(userService.checkEmailRegistered("test@exchange.com", 2));
    }

    @Test
    void testCheckExchangeEmailRegistered_False() {
        lenient().when(exchangeUserRepository.findByPrivateEmail("test@exchange.com")).thenReturn(Optional.of(user));
        assertFalse(userService.checkEmailRegistered("test@exchange.com", 1));
    }

    @Test
    void testCheckHomeEmailRegistered_True() {
        lenient().when(abstractUserRepository.findByHomeEmail("test@home.com")).thenReturn(Optional.empty());
        assertFalse(userService.checkEmailRegistered("test@home.com", 1));
    }

    @Test
    void testCheckHomeEmailRegistered_False() {
        lenient().when(abstractUserRepository.findByHomeEmail("test@home.com")).thenReturn(Optional.of(user));
        assertFalse(userService.checkEmailRegistered("test@home.com", 1));
    }

    @Test
    void testGetUser_True() {
        lenient().when(abstractUserRepository.findById(1L)).thenReturn(Optional.of(user));

        AbstractUser foundUser = userService.getUser(1L);
        assertEquals(user, foundUser);
    }

    @Test
    void testGetUser_False() {
        when(abstractUserRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserException.class, () -> userService.getUser(1L));
    }

    // Might be an issue since the given password does not comply to the security standards
    @Test
    void testChangePassword_Success() {
        ChangePasswordDto dto = new ChangePasswordDto("password", "newPassword");
        when(abstractUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordValidator.checkPasswordMatch("password", "encodedPassword")).thenReturn(true);
        when(passwordValidator.checkPasswordMatch("newPassword", "encodedPassword")).thenReturn(false);
        when(passwordValidator.validatePasswordStrength("newPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");

        pwChangeLogDto result = userService.changePassword(dto, 1L);

        assertEquals("1", result.updateUser());
        assertEquals("encodedPassword", result.updateContent());
    }

    @Test
    void testChangePassword_WrongPassword() {
        ChangePasswordDto dto = new ChangePasswordDto("wrongPassword", "newPassword");
        when(abstractUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordValidator.checkPasswordMatch("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(WrongPasswordException.class, () -> userService.changePassword(dto, 1L));
    }

    @Test
    void testChangePassword_SamePassword() {
        ChangePasswordDto dto = new ChangePasswordDto("password", "password");
        when(abstractUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordValidator.checkPasswordMatch("password", "encodedPassword")).thenReturn(true);
        when(passwordValidator.checkPasswordMatch("password", "encodedPassword")).thenReturn(true);

        assertThrows(ChangedPasswordSameException.class, () -> userService.changePassword(dto, 1L));
    }

    @Test
    void testChangePassword_WeakPassword() {
        ChangePasswordDto dto = new ChangePasswordDto("password", "weak");
        when(abstractUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordValidator.checkPasswordMatch("password", "encodedPassword")).thenReturn(true);
        when(passwordValidator.checkPasswordMatch("weak", "encodedPassword")).thenReturn(false);
        when(passwordValidator.validatePasswordStrength("weak")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.changePassword(dto, 1L));
    }

    @Test
    void testChangeNationality_Success() {
        user.setRegistrationDate(LocalDate.now().minusDays(10));
        when(abstractUserRepository.findById(1L)).thenReturn(Optional.of(user));
        ChangeNationalityDto dto = new ChangeNationalityDto(Country.REPUBLIC_OF_KOREA);

        assertDoesNotThrow(() -> userService.changeNationality(1L, dto));
    }

    @Test
    void testChangeNationality_Fail_UserNotFound() {
        when(abstractUserRepository.findById(1L)).thenReturn(Optional.empty());
        ChangeNationalityDto dto = new ChangeNationalityDto(Country.REPUBLIC_OF_KOREA);

        assertThrows(NoSuchUserException.class, () -> userService.changeNationality(1L, dto));
    }

    @Test
    void testChangeNationality_GracePeriodPassed() {
        user.setRegistrationDate(LocalDate.now().minusDays(20));
        when(abstractUserRepository.findById(1L)).thenReturn(Optional.of(user));
        ChangeNationalityDto dto = new ChangeNationalityDto(Country.REPUBLIC_OF_KOREA);

        assertThrows(PassedGracePeriodException.class, () -> userService.changeNationality(1L, dto));
    }

    @Test
    void testChangeNationality_GracePeriodPassed_GreyZone() {
        user.setRegistrationDate(LocalDate.now().minusDays(14));
        when(abstractUserRepository.findById(1L)).thenReturn(Optional.of(user));
        ChangeNationalityDto dto = new ChangeNationalityDto(Country.REPUBLIC_OF_KOREA);

        assertDoesNotThrow(() -> userService.changeNationality(1L, dto));
    }

    @Test
    void testDeleteUser_Success() {
        DeleteUserDto dto = new DeleteUserDto("password", 1L, "home@example.com");
        when(abstractUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordValidator.checkPasswordMatch("password", "encodedPassword")).thenReturn(true);

        accountDeleteLogDto result = userService.deleteUser(dto);

        assertEquals("1", result.updateUser());
        assertEquals("Delete Account", result.updateContent());
    }

    @Test
    void testDeleteUser_WrongPassword() {
        DeleteUserDto dto = new DeleteUserDto("wrongPassword", 1L, "home@example.com");
        when(abstractUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordValidator.checkPasswordMatch("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(WrongPasswordException.class, () -> userService.deleteUser(dto));
    }
}
