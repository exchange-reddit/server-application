package com.omniversity.server.user.ExchangeUser;

import com.omniversity.server.service.HashValidator;
import com.omniversity.server.user.AllUser.UserService;
import com.omniversity.server.user.ExchangeUser.dto.request.RegistrationDto;
import com.omniversity.server.user.ExchangeUser.dto.request.UpdateExchangeUserDto;
import com.omniversity.server.user.ExchangeUser.dto.response.ExchangeUserResponseNoPasswordDto;
import com.omniversity.server.user.ExchangeUser.dto.response.FriendSuggestionDto;
import com.omniversity.server.user.ExchangeUser.dto.response.PublicUserProfileDto;
import com.omniversity.server.user.ExchangeUser.dto.response.ReturnDto;
import com.omniversity.server.user.ExchangeUser.mapper.functional.ExchangeUserMapper;
import com.omniversity.server.user.ExchangeUser.mapper.functional.UpdateExchangeUserMapper;
import com.omniversity.server.user.ExchangeUser.mapper.response.ExchangeUserResponse;
import com.omniversity.server.user.entity.Enums.Country;
import com.omniversity.server.user.entity.Enums.Gender;
import com.omniversity.server.user.entity.Enums.Language;
import com.omniversity.server.user.entity.Enums.Program;
import com.omniversity.server.user.entity.Enums.University;
import com.omniversity.server.user.entity.ExchangeUser;
import com.omniversity.server.user.exception.HashValidationFailedException;
import com.omniversity.server.user.exception.NoSuchUserException;
import com.omniversity.server.user.exception.UserAlreadyExistsException;
import com.omniversity.server.service.PasswordValidator;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeUserServiceTest {

    @Mock
    private ExchangeUserRepository exchangeUserRepository;

    @Mock
    private UserService userService;

    @Mock
    private ExchangeUserMapper exchangeUserMapper;

    @Mock
    private UpdateExchangeUserMapper updateExchangeUserMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ExchangeUserResponse exchangeUserResponse;

    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private HashValidator hashValidator;

    @InjectMocks
    private ExchangeUserService exchangeUserService;

    private ExchangeUser user;
    private RegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        user = new ExchangeUser();
        user.setId(1L);
        user.setUserName("testUser");
        user.setPrivateEmail("test@example.com");

        registrationDto = new RegistrationDto(
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
    }
    @Test
    void testRegisterUser_Success() throws HashValidationFailedException {
        when(userService.checkEmailRegistered(any(), any(Integer.class))).thenReturn(false);
        when(userService.checkUserNameTaken("testUser")).thenReturn(false);
        when(passwordValidator.validatePasswordStrength("password")).thenReturn(true);
        when(exchangeUserMapper.toEntity(registrationDto)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(hashValidator.validateHash(any(), any())).thenReturn(true);

        ExchangeUser registeredUser = exchangeUserService.registerExchangeUser(registrationDto);

        assertEquals(user, registeredUser);
    }

    @Test
    void testRegisterUser_UserNameExists() {
        when(userService.checkUserNameTaken("testUser")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> exchangeUserService.registerExchangeUser(registrationDto));
    }

    @Test
    void testRegisterUser_EmailExists() {
        when(userService.checkEmailRegistered(any(), any(Integer.class))).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> exchangeUserService.registerExchangeUser(registrationDto));
    }

    @Test
    void getExchangeUser_Success() {
        when(exchangeUserRepository.findById(1L)).thenReturn(Optional.of(user));
        ExchangeUser foundUser = exchangeUserService.getUser(1L);
        assertEquals(user, foundUser);
    }

    @Test
    void getExchangeUser_Fail() {
        when(exchangeUserRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> exchangeUserService.getUser(1L));
    }

    @Test
    void getPublicUserProfileUserName_Success() {
        when(exchangeUserRepository.findByUserName("testUser")).thenReturn(Optional.of(user));

        PublicUserProfileDto expectedDto = new PublicUserProfileDto(
                "Test",
                "User",
                LocalDate.of(2000, 1, 1),
                University.KOREA_UNIVERSITY,
                "testUser",
                null
        );

        when(exchangeUserResponse.toPublicUserProfileDto(user)).thenReturn(expectedDto);

        ReturnDto actualDto = exchangeUserService.getPublicUserProfileUserName("testUser");

        assertTrue(actualDto instanceof PublicUserProfileDto);
        assertEquals(expectedDto, actualDto);
    }

    @Test
    void getPublicUserProfileUserName_Fail() {
        when(exchangeUserRepository.findByUserName("testUser")).thenReturn(Optional.empty());

        assertThrows(NoSuchUserException.class, () -> exchangeUserService.getPublicUserProfileUserName("testUser"));
    }

    @Test
    void getPublicUserProfileIdToProfile_Success() {
        when(exchangeUserRepository.findById(1L)).thenReturn(Optional.of(user));

        PublicUserProfileDto expectedDto = new PublicUserProfileDto(
                "Test",
                "User",
                LocalDate.of(2000, 1, 1),
                University.KOREA_UNIVERSITY,
                "testUser",
                null
        );

        when(exchangeUserResponse.toPublicUserProfileDto(user)).thenReturn(expectedDto);

        ReturnDto actualDto = exchangeUserService.getPublicUserProfileId(1L, 1);

        assertTrue(actualDto instanceof PublicUserProfileDto);
        assertEquals(expectedDto, actualDto);
    }

    @Test
    void getPublicUserProfileIdToProfile_Fail() {
        when(exchangeUserRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserException.class, () -> exchangeUserService.getPublicUserProfileId(1L, 1));
    }

    @Test
    void getPublicUserProfileIdToFriend_Success() {
        when(exchangeUserRepository.findById(1L)).thenReturn(Optional.of(user));

        FriendSuggestionDto expectedDto = new FriendSuggestionDto(
                "Test",
                "User",
                null,
                University.KOREA_UNIVERSITY,
                Program.COMPUTER_SCIENCE
        );

        when(exchangeUserResponse.toFriendSuggestionDto(user)).thenReturn(expectedDto);

        ReturnDto actualDto = exchangeUserService.getPublicUserProfileId(1L, 2);

        assertTrue(actualDto instanceof FriendSuggestionDto);
        assertEquals(expectedDto, actualDto);
    }

    @Test
    void getPublicUserProfileIdToFriend_Fail() {
        when(exchangeUserRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserException.class, () -> exchangeUserService.getPublicUserProfileId(1L, 2));
    }

    @Test
    void testUpdateUser_Success() {
        UpdateExchangeUserDto dto = new UpdateExchangeUserDto("newUsername", null, null, null);
        when(exchangeUserRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        ExchangeUserResponseNoPasswordDto expectedDto = new ExchangeUserResponseNoPasswordDto(
                "Test",
                "User",
                "test@example.com",
                "newUsername",
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2025, 5, 31),
                University.KOREA_UNIVERSITY,
                University.KTH_ROYAL_INSTITUTE_OF_TECHNOLOGY,
                Program.COMPUTER_SCIENCE,
                Country.UNITED_STATES,
                Language.ENGLISH,
                null
        );

        when(exchangeUserResponse.toResponseExchangeDto(user)).thenReturn(expectedDto);

        ExchangeUserResponseNoPasswordDto actualDto = exchangeUserService.updateExchangeUser(dto, 1L);

        assertDoesNotThrow(() -> exchangeUserService.updateExchangeUser(dto, 1L));
        assertEquals(expectedDto, actualDto);
    }

    @Test
    void testUpdateUser_Fail() {
        UpdateExchangeUserDto dto = new UpdateExchangeUserDto("newUsername", null, null, null);
        when(exchangeUserRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserException.class, () -> exchangeUserService.updateExchangeUser(dto, 1L));
    }

    @Test
    void testGetUserById_Success() {
        when(exchangeUserRepository.findById(1L)).thenReturn(Optional.of(user));
        ExchangeUser foundUser = exchangeUserService.getUser(1L);
        assertEquals(user, foundUser);
    }

    @Test
    void testGetUserById_NotFound() {
        when(exchangeUserRepository.findById(1L)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> exchangeUserService.getUser(1L));
    }
}
