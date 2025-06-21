package com.omniversity.server.user;

import com.omniversity.server.JwtTokenProvider;
import com.omniversity.server.service.Mapper.ExchangeUserMapper;
import com.omniversity.server.service.Mapper.ProspectiveUserMapper;
import com.omniversity.server.user.dto.ExchangeUserRegistrationDto;
import com.omniversity.server.user.dto.LoginInputDto;
import com.omniversity.server.user.dto.LoginOutputDto;
import com.omniversity.server.user.dto.ProspectiveUserRegistrationDto;
import com.omniversity.server.exception.ChangedPasswordSameException;
import com.omniversity.server.exception.NoSuchUserException;
import com.omniversity.server.exception.WrongPasswordException;
import com.omniversity.server.service.PasswordValidator;
import com.omniversity.server.service.Mapper.UpdateUserMapper;
import com.omniversity.server.service.Mapper.UserResponse.UserResponseMapper;
import com.omniversity.server.user.dto.*;
import com.omniversity.server.user.dto.response.ReturnDto;
import com.omniversity.server.user.entity.User;

import com.omniversity.server.user.entity.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.omniversity.server.user.entity.UserType.*;

/**
 * TODO
 * Add email verification step prior to registration
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExchangeUserMapper exchangeUserMapper;
    private final ProspectiveUserMapper prospectiveUserMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final UpdateUserMapper updateUserMapper;
    private final UserResponseMapper userResponseMapper;
    private final PasswordValidator passwordValidator;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ExchangeUserMapper exchangeUserMapper,
            ProspectiveUserMapper prospectiveUserMapper,
            UpdateUserMapper updateUserMapper,
            UserResponseMapper userResponseMapper,
            JwtTokenProvider jwtTokenProvider,
            PasswordValidator passwordValidator
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.exchangeUserMapper = exchangeUserMapper;
        this.prospectiveUserMapper = prospectiveUserMapper;
        this.updateUserMapper = updateUserMapper;
        this.userResponseMapper = userResponseMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordValidator = passwordValidator;
    }

    public User getUser(int id) throws NoSuchUserException {
        return userRepository.findById((long) id)
                .orElseThrow(() -> new NoSuchUserException("The user with ID " + id + " was not found"));
    }

    // An endpoint to return certain part of the user information
    public ReturnDto getPublicUserInfo(int id, int option) {
        User user = getUser(id);

        return switch (option) {
            case (1) -> userResponseMapper.toPublicUserProfileDto(user);
            case (2) -> userResponseMapper.toFriendSuggestionDto(user);
            default -> null;
        };
    }


    // Check whether the user id is taken or not
    public Boolean checkUserIdTaken(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    // Login user
    public LoginOutputDto loginUser(LoginInputDto loginDto) {
        // 1. Find user by email
        User user = userRepository.findByHomeEmail(loginDto.getHomeUniEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // 2. Check password
        if (!passwordValidator.checkPasswordMatch(loginDto.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3. Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        // 4. Return output DTO
        return new LoginOutputDto(
                user.getHomeEmail(),
                accessToken,
                refreshToken
        );
    }

    // Registers exchange users
    public User registerExchangeUser(ExchangeUserRegistrationDto dto) {
        // Check if the private email entered by the user has been taken by a pre-existing account or not.
        if (userRepository.findByPrivateEmail(dto.getPrivateEmail()).isPresent()) {
            throw new RuntimeException("Private email already registered.");
        }
        // Check if the home email that the user is trying to use to verify the account has been used or not.
        if (userRepository.findByHomeEmail(dto.getHomeEmail()).isPresent()) {
            throw new RuntimeException("Home email already in use.");
        }
        // Check if the exchange email that the user is trying to use to verify the account has been used or not.
        if (userRepository.findByExchangeEmail(dto.getExchangeEmail()).isPresent()) {
            throw new RuntimeException("Exchange email already in use.");
        }
        // Check if the user id has been taken by an another user or not.
        if (checkUserIdTaken(dto.getUserId())) {
            throw new RuntimeException("Username is already taken.");
        }

        // Using mapper library to avoid redundant code
        User user = exchangeUserMapper.toEntity(dto);
        user.setUserType(EXCHANGE_USER);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setActive(true);

        return userRepository.save(user);
    }


    // Registers prospective exchange student users
    public User registerProspectiveUser(ProspectiveUserRegistrationDto dto) {
        // Check if the private email entered by the user has been taken by a pre-existing account or not.
        if (userRepository.findByPrivateEmail(dto.privateEmail()).isPresent()) {
            throw new RuntimeException("Private email already registered.");
        }
        // Check if the home email that the user is trying to use to verify the account has been used or not.
        if (userRepository.findByHomeEmail(dto.homeEmail()).isPresent()) {
            throw new RuntimeException("Home email already in use.");
        }
        // Check if the user id has been taken by an another user or not.
        if (checkUserIdTaken(dto.userId())) {
            throw new RuntimeException("Username is already taken.");
        }

        if (!passwordValidator.validatePasswordStrength(dto.password())) {
            throw new RuntimeException("Password does not meet security requirements.");
        }

        User user = prospectiveUserMapper.toEntity(dto);
        user.setUserType(PROSPECTIVE_USER);
        user.setPasswordHash(passwordEncoder.encode(dto.password()));

        return userRepository.save(user);
    }


    public void changePassword(ChangePasswordDto changePasswordDto, int id) {
        User user = getUser(id);

        // Verify if the provided current password is correct
        if (!passwordValidator.checkPasswordMatch(changePasswordDto.currentPassword(), user.getPasswordHash())) {
            throw new WrongPasswordException("Provided password is incorrect");
        }

        // Verify if the new password is the same as the old one.
        if (passwordValidator.checkPasswordMatch(changePasswordDto.newPassword(), user.getPasswordHash())) {
            throw new ChangedPasswordSameException("New password cannot be the same as the old password.");
        }

        // Verify the strength of the new password
        if (!passwordValidator.validatePasswordStrength(changePasswordDto.newPassword())) {
            throw new RuntimeException("Password must be secure");
        }

        // TODO: Choice of email (Between home, exchange, and private)

        // Set new password for the user
        user.setPasswordHash(passwordEncoder.encode(changePasswordDto.newPassword()));
        userRepository.save(user);
    }

    public Object updateAccount(UpdateAccountDto updateAccountDto, int id) {
        User user = getUser(id);
        UserType userType = user.getUserType();

        Object response;

        updateUserMapper.updateEntity(user, updateAccountDto);

        userRepository.save(user);

        switch (userType) {
            case EXCHANGE_USER -> response = userResponseMapper.toResponseExchangeDto(user);
            case PROSPECTIVE_USER -> response = userResponseMapper.toResponseProspectiveDto(user);
            default -> response = null;
        }

        return response;

    }

    public Boolean deleteUser(DeleteUserDto deleteUserDto) {
        try {
            // Retrieve the user from the database or throw NoSuchUserException
            User user = getUser((int)deleteUserDto.userId());

            // Validate if the provided password is correct
            if (!passwordValidator.checkPasswordMatch(deleteUserDto.password(), user.getPasswordHash())) {
                throw new WrongPasswordException("Invalid password provided for user deletion");
            }

            // Remove password hash from the user object to avoid possible data breach
            user.setPasswordHash("");

            userRepository.deleteById(user.getId());
            return true;

        } catch (NoSuchUserException | WrongPasswordException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting the user: " + e.getMessage(), e);
        }

    }

}
