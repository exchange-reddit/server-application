package com.omniversity.server.user;

import com.omniversity.server.exception.NoSuchUserException;
import com.omniversity.server.exception.WrongPasswordException;
import com.omniversity.server.service.ExchangeUserMapper;
import com.omniversity.server.service.ProspectiveUserMapper;
import com.omniversity.server.user.dto.DeleteUserDto;
import com.omniversity.server.user.dto.ExchangeUserRegistrationDto;
import com.omniversity.server.user.dto.ProspectiveUserRegistrationDto;
import com.omniversity.server.user.entity.User;

import com.omniversity.server.user.entity.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static com.omniversity.server.user.entity.UserType.*;

/**
 * TODO
 * Use 'Mapstruct' rather than manual mapping to reduce redundant codes.
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

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ExchangeUserMapper exchangeUserMapper, ProspectiveUserMapper prospectiveUserMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.exchangeUserMapper = exchangeUserMapper;
        this.prospectiveUserMapper = prospectiveUserMapper;
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

        user.setPasswordHash(passwordEncoder.encode(prospectiveUserRegistrationDto.password()));
        user.setUserType(PROSPECTIVE_USER);

        return userRepository.save(user);

    }

    // Validate user password (A private method)
    // Trying to avoid using JWT for this case to avoid malicious attacks
    private Boolean validatePW(String hashValue, String challengePW) {
        try {
            // If the pw matches, return true
            if (passwordEncoder.matches(challengePW, hashValue)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean deleteUser(DeleteUserDto deleteUserDto) {
        try {
            // Retrieve the user from the database or throw NoSuchUserException
            Optional<User> possibleUser = Optional.ofNullable(userRepository.findById(deleteUserDto.userId()));

            if (possibleUser.isEmpty()) {
                throw new NoSuchUserException("No user found with ID: " + deleteUserDto.userId());
            }

            User user = possibleUser.get();

            if (!validatePW(user.getPasswordHash(), deleteUserDto.password())) {
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
