package com.omniversity.server.user;

import com.omniversity.server.user.dto.ExchangeUserRegistrationDTO;
import com.omniversity.server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.omniversity.server.user.entity.UserType.*;

/**
 * TODO
 * Use 'Mapstruct' rather than manual mapping to reduce redundant codes.
 */
@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(ExchangeUserRegistrationDTO exchangeUserRegistrationDTO) {

        // Check if the private email entered by the user has been taken by a pre-existing account or not.
        if (userRepository.findByPrivateEmail(exchangeUserRegistrationDTO.getHomeEmail()).size() != 0) {
            throw new RuntimeException("Error: You have already created an account with this email!");
        }

        // Check if the home email that the user is trying to use to verify the account has been used or not.
        if (userRepository.findByHomeEmail(exchangeUserRegistrationDTO.getHomeEmail()).size() != 0) {
            throw new RuntimeException("Error: This email has already been used by an another account for verification!");
        }

        // Check if the exchange email that the user is trying to use to verify the account has been used or not.
        if (userRepository.findByExchangeEmail(exchangeUserRegistrationDTO.getExchangeEmail()).size() != 0) {
            throw new RuntimeException("Error: This email has already been used by an other account for verification!");
        }

        // Check if the user id has been taken by an other user or not.
        if (userRepository.findByUserId(exchangeUserRegistrationDTO.getUserId()).size() != 0) {
            throw new RuntimeException("Error: This username has already been used by an another user.");
        }

        User user = new User();
        user.setName(exchangeUserRegistrationDTO.getName());
        user.setPrivateEmail(exchangeUserRegistrationDTO.getPrivateEmail());
        user.setUserId(exchangeUserRegistrationDTO.getUserId());
        user.setDateOfBirth(exchangeUserRegistrationDTO.getDateOfBirth());
        user.setUserType(EXCHANGE_USER);
        user.setHomeUni(exchangeUserRegistrationDTO.getHomeUni());
        user.setExchangeUni(exchangeUserRegistrationDTO.getExchangeUni());
        user.setHomeEmail(exchangeUserRegistrationDTO.getHomeEmail());
        user.setExchangeEmail(exchangeUserRegistrationDTO.getExchangeEmail());
        user.setNationality(exchangeUserRegistrationDTO.getNationality());
        user.setExchangeStart(exchangeUserRegistrationDTO.getExchangeStart());
        user.setExchangeEnd(exchangeUserRegistrationDTO.getExchangeEnd());
        user.setPreferredLanguage(exchangeUserRegistrationDTO.getPreferredLanguage());
        user.setPasswordHash(passwordEncoder.encode(exchangeUserRegistrationDTO.getPassword()));

        return userRepository.save(user);
    }
}
