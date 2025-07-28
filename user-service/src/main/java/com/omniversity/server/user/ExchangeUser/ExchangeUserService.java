package com.omniversity.server.user.ExchangeUser;


import com.omniversity.server.service.HashValidator;
import com.omniversity.server.user.exception.*;
import com.omniversity.server.user.ExchangeUser.dto.request.UpdateExchangeUserDto;
import com.omniversity.server.user.ExchangeUser.dto.response.ExchangeUserResponseNoPasswordDto;
import com.omniversity.server.user.ExchangeUser.mapper.functional.ExchangeUserMapper;
import com.omniversity.server.service.PasswordValidator;
import com.omniversity.server.user.ExchangeUser.dto.request.RegistrationDto;
import com.omniversity.server.user.ExchangeUser.dto.response.ReturnDto;
import com.omniversity.server.user.ExchangeUser.mapper.functional.UpdateExchangeUserMapper;
import com.omniversity.server.user.ExchangeUser.mapper.response.ExchangeUserResponse;
import com.omniversity.server.user.AllUser.UserService;
import com.omniversity.server.user.entity.ExchangeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ExchangeUserService {
    private final ExchangeUserRepository exchangeUserRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ExchangeUserMapper exchangeUserMapper;
    private final UpdateExchangeUserMapper updateExchangeUserMapper;
    private final ExchangeUserResponse exchangeUserResponse;
    private final PasswordValidator passwordValidator;
    private final HashValidator hashValidator;



    @Autowired
    public ExchangeUserService(ExchangeUserRepository exchangeUserRepository, UserService userService, PasswordEncoder passwordEncoder, ExchangeUserMapper exchangeUserMapper, UpdateExchangeUserMapper updateExchangeUserMapper, ExchangeUserResponse exchangeUserResponse, PasswordValidator passwordValidator, HashValidator hashValidator) {
        this.exchangeUserRepository = exchangeUserRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.exchangeUserMapper = exchangeUserMapper;
        this.updateExchangeUserMapper = updateExchangeUserMapper;
        this.exchangeUserResponse = exchangeUserResponse;
        this.passwordValidator = passwordValidator;
        this.hashValidator = hashValidator;
    }

    public ExchangeUser getUser(long id) throws NoSuchUserException {
        ExchangeUser user = exchangeUserRepository.findById(id)
                .orElseThrow(() ->
                {
                    throw new NoSuchUserException("The user with the provided ID was not found: " + id);
                });
        return user;
    }

    public ReturnDto getPublicUserProfileUserName (String userName) throws NoSuchUserException {
        ExchangeUser user = exchangeUserRepository.findByUserName(userName)
                .orElseThrow(() ->
                {
                    throw new NoSuchUserException(String.format("The user with the provided username was not found: %s", userName));
                });


        return exchangeUserResponse.toPublicUserProfileDto(user);
    }

    public ReturnDto getPublicUserProfileId (long id, int option) throws NoSuchUserException {
        ExchangeUser user = exchangeUserRepository.findById(id)
                .orElseThrow(() ->
                {
                    throw new NoSuchUserException(String.format("The user with the provided ID was not found: %d", id));
                });

        switch (option) {
            case (1):
                return exchangeUserResponse.toPublicUserProfileDto(user);
            case (2):
                return exchangeUserResponse.toFriendSuggestionDto(user);
        }

        return null;
    }

    public ExchangeUser registerExchangeUser (RegistrationDto dto) throws UserAlreadyExistsException, WeakPasswordException, VerificationRequestError, RegistrationFailedException, HashValidationFailedException {
        // Check if exchange email was already used
        if (userService.checkEmailRegistered(dto.exchangeEmail(), 2)) {
            throw new UserAlreadyExistsException(String.format("You have already created an account using this email: %s", dto.exchangeEmail()));
        }

        // Check if home email was already used
        if (userService.checkEmailRegistered(dto.homeEmail(), 3)) {
            throw new UserAlreadyExistsException(String.format("You have already created an account using this email: %s", dto.homeEmail()));
        }

        // Check if username was already taken
        if (userService.checkUserNameTaken(dto.userName())) {
            throw new UserAlreadyExistsException(String.format("The username %s has already been taken by an another user", dto.userName()));
        }

        if (!passwordValidator.validatePasswordStrength(dto.password())) {
            throw new WeakPasswordException("The provided password does not comply to the security requirement.");
        }

        // Here, we assume that the client's local storage has received the validation hash for each emails.
        // Validate Home Email
        if (!hashValidator.validateHash(dto.homeHash(), dto.homeEmail())) {
            throw new HashValidationFailedException("An error occurred while validating your home university email address.");
        }

        // Validate Exchange Email
        if (!hashValidator.validateHash(dto.exchangeHash(), dto.exchangeEmail())) {
            throw new HashValidationFailedException("An error occurred while validating your exchange university email address.");
        }

        ExchangeUser user = exchangeUserMapper.toEntity(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        user.setIsActive(true);

        exchangeUserRepository.save(user);

        return user;
    }

    public ExchangeUserResponseNoPasswordDto updateExchangeUser (UpdateExchangeUserDto dto, long id) throws NoSuchUserException {
        ExchangeUser user = getUser(id);

        updateExchangeUserMapper.updateEntity(user, dto);

        exchangeUserRepository.save(user);

        return exchangeUserResponse.toResponseExchangeDto(user);
    }
}
