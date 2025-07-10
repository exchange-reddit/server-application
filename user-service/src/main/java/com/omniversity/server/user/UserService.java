package com.omniversity.server.user;

import com.omniversity.server.exception.ChangedPasswordSameException;
import com.omniversity.server.exception.NoSuchUserException;
import com.omniversity.server.exception.PassedGracePeriodException;
import com.omniversity.server.exception.WrongPasswordException;
import com.omniversity.server.log.dto.accountDeleteLogDto;
import com.omniversity.server.log.dto.pwChangeLogDto;
import com.omniversity.server.service.Mapper.ExchangeUserMapper;
import com.omniversity.server.service.PasswordValidator;
import com.omniversity.server.service.Mapper.ProspectiveUserMapper;
import com.omniversity.server.service.Mapper.UpdateUserMapper;
import com.omniversity.server.service.Mapper.UserResponse.UserResponseMapper;
import com.omniversity.server.user.dto.*;
import com.omniversity.server.user.dto.response.ReturnDto;
import com.omniversity.server.user.entity.Country;
import com.omniversity.server.user.entity.User;

import com.omniversity.server.user.entity.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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
    private UpdateUserMapper updateUserMapper;
    private UserResponseMapper userResponseMapper;
    private PasswordValidator passwordValidator;

    @Autowired
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

    // TODO: This method needs to return a pwChangeLogDto so that it can be saved in db
    public pwChangeLogDto changePassword(ChangePasswordDto changePasswordDto, int id) {
        try {
            User user = getUser(id);
            String prevHash = user.getPasswordHash();

            // Verify if the provided current password is correct
            if (!passwordValidator.checkPasswordMatch(changePasswordDto.currentPassword(), prevHash)) {
                throw new WrongPasswordException("Provided password is incorrect");
            }

            // Verify if the new password is the same as the old one.
            if (passwordValidator.checkPasswordMatch(changePasswordDto.newPassword(), prevHash)) {
                throw new ChangedPasswordSameException("New password cannot be the same as the old password.");
            }

            // Verify the strength of the new password
            if (!passwordValidator.validatePasswordStrength(changePasswordDto.newPassword())) {
                throw new RuntimeException("Password must be secure");
            }

            // TODO: Choice of email (Between home, exchange, and private)

            // Calculate and save the hash value of the new provided password
            user.setPasswordHash(passwordEncoder.encode(changePasswordDto.newPassword()));
            // Save the user to the database
            userRepository.save(user);

            // Send the user object with the previous password hash
            user.setPasswordHash(prevHash);

            // Return the pwChangeLog in a form of dto
            return new pwChangeLogDto(String.valueOf(id), prevHash);

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

    // A dedicated method to assist users to change their nationality (Users are allowed to change their nationality for once during the grace period of 2 weeks)
    public void changeNationality(int id, ChangeNationalityDto dto) throws PassedGracePeriodException {
        // Get requesting user through their id
        User user = getUser(id);
        // Retrieve the server date of the day of operation (Server Time)
        LocalDate today = LocalDate.now();
        // Convert the user registration date object (Date) into a LocalDate object for comparison. (Based on server time)
        LocalDate registrationDate = user.getRegistrationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // Calculate the days between the registration date and the date of request
        long daysBetween = ChronoUnit.DAYS.between(today, registrationDate);

        // Check if the grace period has passed or not
        if (daysBetween > 14) {
            throw new PassedGracePeriodException(String.format("%d days have passed since your registration to our service. The grace period to change your nationality is 2 weeks.", daysBetween));
        }

        user.setNationality(dto.nationality());
        userRepository.save(user);
    }

    // Method to delete an account
    // TODO: Add an additional method to delete account in case the user forgets password (By Email preferably)
    public accountDeleteLogDto deleteUser(DeleteUserDto deleteUserDto) throws WrongPasswordException {
        // Retrieve the user that will be deleted
        User user = getUser((int)deleteUserDto.userId());

        if (!passwordValidator.checkPasswordMatch(deleteUserDto.password(), user.getPasswordHash())) {
            throw new WrongPasswordException("The provided previous password does not match.");
        }

        userRepository.deleteById(user.getId());

        return new accountDeleteLogDto(String.valueOf(deleteUserDto.userId()), "Delete Account");

    }

}
