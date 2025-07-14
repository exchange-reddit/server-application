package com.omniversity.server.user.ProspectiveUser;

import com.omniversity.server.service.HashValidator;
import com.omniversity.server.service.PasswordValidator;
import com.omniversity.server.user.AllUser.UserService;
import com.omniversity.server.user.ProspectiveUser.dto.request.DesiredUniversityUpdateDto;
import com.omniversity.server.user.ProspectiveUser.dto.request.ProspectiveUserRegistrationDto;
import com.omniversity.server.user.ProspectiveUser.dto.response.ProspectiveUserResponseNoPasswordDto;
import com.omniversity.server.user.ProspectiveUser.mapper.functional.ProspectiveUserMapper;
import com.omniversity.server.user.ProspectiveUser.mapper.response.ProspectiveUserResponse;
import com.omniversity.server.user.entity.Enums.Country;
import com.omniversity.server.user.entity.Enums.Gender;
import com.omniversity.server.user.entity.Enums.Language;
import com.omniversity.server.user.entity.Enums.Program;
import com.omniversity.server.user.entity.Enums.University;
import com.omniversity.server.user.entity.ProspectiveUser;
import com.omniversity.server.user.exception.HashValidationFailedException;
import com.omniversity.server.user.exception.NoSuchUserException;
import com.omniversity.server.user.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProspectiveUserServiceTest {

    @Mock
    private ProspectiveUserRepository prospectiveUserRepository;

    @Mock
    private ProspectiveUserMapper prospectiveUserMapper;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private HashValidator hashValidator;

    @Mock
    private ProspectiveUserResponse prospectiveUserResponse;

    @InjectMocks
    private ProspectiveUserService prospectiveUserService;

    private ProspectiveUser user;
    private ProspectiveUserRegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        user = new ProspectiveUser();
        user.setId(1L);
        user.setUserName("testUser");
        user.setPrivateEmail("test@example.com");

        registrationDto = new ProspectiveUserRegistrationDto(
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
    }

    @Test
    void testRegisterUser_Success() throws HashValidationFailedException {
        when(userService.checkUserNameTaken("testUser")).thenReturn(false);
        when(userService.checkEmailRegistered("home@example.com", 3)).thenReturn(false);
        when(passwordValidator.validatePasswordStrength("password")).thenReturn(true);

        when(prospectiveUserMapper.toEntity(registrationDto)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(hashValidator.validateHash(any(), any())).thenReturn(true);

        ProspectiveUser registeredUser = prospectiveUserService.registerProspectiveUser(registrationDto);

        assertEquals(user, registeredUser);
    }

    @Test
    void testRegisterUser_UserNameExists() {
        when(userService.checkUserNameTaken("testUser")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> prospectiveUserService.registerProspectiveUser(registrationDto));
    }

    @Test
    void testRegisterUser_EmailExists() {
        when(userService.checkEmailRegistered("home@example.com", 3)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> prospectiveUserService.registerProspectiveUser(registrationDto));
    }

    @Test
    void testUpdateDesiredExchangeUniversity_Success() {
        DesiredUniversityUpdateDto reqDto = new DesiredUniversityUpdateDto(University.STOCKHOLM_UNIVERSITY);
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

        when(prospectiveUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(prospectiveUserResponse.toResponseProspectiveDto(user)).thenReturn(expectedDto);

        ProspectiveUserResponseNoPasswordDto actualDto = prospectiveUserService.updateDesiredExchangeUniversity(reqDto, 1L);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void testUpdateDesiredExchangeUniversity_Fail() {
        DesiredUniversityUpdateDto reqDto = new DesiredUniversityUpdateDto(University.STOCKHOLM_UNIVERSITY);

        when(prospectiveUserRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserException.class, () -> prospectiveUserService.updateDesiredExchangeUniversity(reqDto, 1L));
    }
}
