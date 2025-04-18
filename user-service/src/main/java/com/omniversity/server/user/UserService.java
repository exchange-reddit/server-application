package com.omniversity.server.user;

import com.omniversity.server.service.ExchangeUserMapper;
import com.omniversity.server.service.ProspectiveUserMapper;
import com.omniversity.server.user.dto.ExchangeUserRegistrationDto;
import com.omniversity.server.user.dto.ProspectiveUserRegistrationDto;
import com.omniversity.server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.omniversity.server.user.entity.UserType.*;

/**
 * TODO
 * Use 'Mapstruct' rather than manual mapping to reduce redundant codes.
 * Add email verification step prior to registration
 */
@Service
public class UserService {

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

    // Registers exchange users
    public User registerExchangeUser(ExchangeUserRegistrationDto exchangeUserRegistrationDto) {

        // Check if the private email entered by the user has been taken by a pre-existing account or not.
        if (userRepository.findByPrivateEmail(exchangeUserRegistrationDto.getPrivateEmail()).size() != 0) {
            throw new RuntimeException("Error: You have already created an account with this email!");
        }

        // Check if the home email that the user is trying to use to verify the account has been used or not.
        if (userRepository.findByHomeEmail(exchangeUserRegistrationDto.getHomeEmail()).size() != 0) {
            throw new RuntimeException("Error: This email has already been used by an another account for verification!");
        }

        // Check if the exchange email that the user is trying to use to verify the account has been used or not.
        if (userRepository.findByExchangeEmail(exchangeUserRegistrationDto.getExchangeEmail()).size() != 0) {
            throw new RuntimeException("Error: This email has already been used by an other account for verification!");
        }

        // Check if the user id has been taken by an another user or not.
        if (userRepository.findByUserId(exchangeUserRegistrationDto.getUserId()).size() != 0) {
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
        if (userRepository.findByPrivateEmail(prospectiveUserRegistrationDto.privateEmail()).size() != 0) {
            throw new RuntimeException("Error: You have already created an account with this email!");
        }

        // Check if the home email that the user is trying to use to verify the account has been used or not.
        if (userRepository.findByHomeEmail(prospectiveUserRegistrationDto.homeEmail()).size() != 0) {
            throw new RuntimeException("Error: This email has already been used by an another account for verification!");
        }

        // Check if the home email that the user is trying to use to verify the account has been used or not.
        if (userRepository.findByHomeEmail(prospectiveUserRegistrationDto.userId()).size() != 0) {
            throw new RuntimeException("Error: This id has already been taken by an another user!");
        }

        User user = prospectiveUserMapper.toEntity(prospectiveUserRegistrationDto);

        user.setPasswordHash(passwordEncoder.encode(prospectiveUserRegistrationDto.password()));
        user.setUserType(PROSPECTIVE_USER);

        return userRepository.save(user);

    }

}
