package com.omniversity.server.user;

import com.omniversity.server.JwtTokenProvider;
import com.omniversity.server.service.ExchangeUserMapper;
import com.omniversity.server.service.ProspectiveUserMapper;
import com.omniversity.server.user.dto.ExchangeUserRegistrationDto;
import com.omniversity.server.user.dto.LoginInputDto;
import com.omniversity.server.user.dto.LoginOutputDto;
import com.omniversity.server.user.dto.ProspectiveUserRegistrationDto;
import com.omniversity.server.exception.ChangedPasswordSameException;
import com.omniversity.server.exception.NoSuchUserException;
import com.omniversity.server.exception.WrongPasswordException;
import com.omniversity.server.service.Mapper.ExchangeUserMapper;
import com.omniversity.server.service.PasswordValidator;
import com.omniversity.server.service.Mapper.ProspectiveUserMapper;
import com.omniversity.server.service.Mapper.UpdateUserMapper;
import com.omniversity.server.service.Mapper.UserResponse.UserResponseMapper;
import com.omniversity.server.user.dto.*;
import com.omniversity.server.user.dto.response.ReturnDto;
import com.omniversity.server.user.entity.User;

import com.omniversity.server.user.entity.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static com.omniversity.server.user.entity.UserType.*;

/**
 * TODO
 * Add email verification step prior to registration
 */
@Service
public class UserService {
    @Value("http://localhost:8080")
    private String baseUrl;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ExchangeUserMapper exchangeUserMapper;
    private ProspectiveUserMapper prospectiveUserMapper;
    private JwtTokenProvider jwtTokenProvider;
    private UpdateUserMapper updateUserMapper;
    private UserResponseMapper userResponseMapper;
    private PasswordValidator passwordValidator;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            \ExchangeUserMapper exchangeUserMapper,
            ProspectiveUserMapper prospectiveUserMapper,
            JwtTokenProvider jwtTokenProvider
    ) {
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ExchangeUserMapper exchangeUserMapper, ProspectiveUserMapper prospectiveUserMapper, PasswordValidator passwordValidator, UpdateUserMapper updateUserMapper, UserResponseMapper userResponseMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.exchangeUserMapper = exchangeUserMapper;
        this.prospectiveUserMapper = prospectiveUserMapper;
        this.updateUserMapper = updateUserMapper;
        this.userResponseMapper = userResponseMapper;
        this.passwordValidator = passwordValidator;
    }

    public User getUser(int id) throws NoSuchUserException{
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findById(id));

        if (optionalUser.isEmpty()) {
            throw new NoSuchUserException("The user with following Id was not found: " + id);
        }

        return optionalUser.get();
    }

    // An endpoint to return certain part of the user information
    public ReturnDto getPublicUserInfo(int id, int option) {
        try {
            User user = getUser(id);

            switch (option) {
                case (1):
                    return userResponseMapper.toPublicUserProfileDto(user);
                case (2):
                    return userResponseMapper.toFriendSuggestionDto(user);
                default:
                    return null;
            }
        } catch (Exception e) {
            throw e;
        }
    }


    // Check whether the user id is taken or not
    public Boolean checkUserIdTaken(String userId) {
        return Optional.ofNullable(userRepository.findByUserId(userId)).isPresent();
    }

    // Login user
    public LoginOutputDto loginUser(LoginInputDto loginDto) {

    }

    // Registers exchange users
    public User registerExchangeUser(ExchangeUserRegistrationDto exchangeUserRegistrationDto) {

        // Check if the private email entered by the user has been taken by a pre-existing account or not.
        if (Optional.ofNullable(userRepository.findByPrivateEmail(exchangeUserRegistrationDto.getPrivateEmail())).isPresent()) {
            throw new RuntimeException("Error: You have already created an account with this email!");
        }

        // Check if the home email that the user is trying to use to verify the account has been used or not.
        if (Optional.ofNullable(userRepository.findByHomeEmail(exchangeUserRegistrationDto.getHomeEmail())).isPresent()) {
            throw new RuntimeException("Error: This email has already been used by an another account for verification!");
        }

        // Check if the exchange email that the user is trying to use to verify the account has been used or not.
        if (Optional.ofNullable(userRepository.findByExchangeEmail(exchangeUserRegistrationDto.getExchangeEmail())).isPresent()) {
            throw new RuntimeException("Error: This email has already been used by an other account for verification!");
        }

        // Check if the user id has been taken by an another user or not.
        if (checkUserIdTaken(exchangeUserRegistrationDto.getUserId())) {
            throw new RuntimeException("Error: This username has already been used by an another user.");
        }

        // Using mapper library to avoid redundant code
        User user = exchangeUserMapper.toEntity(exchangeUserRegistrationDto);
        user.setUserType(EXCHANGE_USER);
        user.setPasswordHash(passwordEncoder.encode(exchangeUserRegistrationDto.getPassword()));
        user.setActive(true);

        return userRepository.save(user);
    }

    // Registers prospective exchange student users
    public User registerProspectiveUser(ProspectiveUserRegistrationDto prospectiveUserRegistrationDto) {
        // Check if the private email entered by the user has been taken by a pre-existing account or not.
        if (Optional.ofNullable(userRepository.findByPrivateEmail(prospectiveUserRegistrationDto.privateEmail())).isPresent()) {
            throw new RuntimeException("Error: You have already created an account with this email!");
        }

        // Check if the home email that the user is trying to use to verify the account has been used or not.
        if (Optional.ofNullable(userRepository.findByHomeEmail(prospectiveUserRegistrationDto.homeEmail())).isPresent()) {
            throw new RuntimeException("Error: This email has already been used by an another account for verification!");
        }

        // Check if the user id has been taken by an another user or not.
        if (checkUserIdTaken(prospectiveUserRegistrationDto.userId())) {
            throw new RuntimeException("Error: This username has already been used by an another user.");
        }

        User user = prospectiveUserMapper.toEntity(prospectiveUserRegistrationDto);

        if (!passwordValidator.validatePasswordStrength(prospectiveUserRegistrationDto.password())) {
            throw new RuntimeException("Error: The provided password does not meet the security requirement.");
        }

        user.setPasswordHash(passwordEncoder.encode(prospectiveUserRegistrationDto.password()));
        user.setUserType(PROSPECTIVE_USER);

        return userRepository.save(user);

    }

    public void changePassword(ChangePasswordDto changePasswordDto, int id) {
        try {
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

        } catch (Exception e) {
            throw e;
        }
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
