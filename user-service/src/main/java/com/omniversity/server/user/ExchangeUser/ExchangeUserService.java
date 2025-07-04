package com.omniversity.server.user.ExchangeUser;

import com.omniversity.server.user.exception.NoSuchUserException;
import com.omniversity.server.user.ExchangeUser.dto.request.UpdateExchangeUserDto;
import com.omniversity.server.user.ExchangeUser.dto.response.ExchangeUserResponseNoPasswordDto;
import com.omniversity.server.user.ExchangeUser.mapper.functional.ExchangeUserMapper;
import com.omniversity.server.service.PasswordValidator;
import com.omniversity.server.user.ExchangeUser.dto.request.RegistrationDto;
import com.omniversity.server.user.ExchangeUser.dto.response.ReturnDto;
import com.omniversity.server.user.exception.UserAlreadyExistsException;
import com.omniversity.server.user.ExchangeUser.mapper.functional.UpdateExchangeUserMapper;
import com.omniversity.server.user.ExchangeUser.mapper.response.ExchangeUserResponse;
import com.omniversity.server.user.AllUser.UserService;
import com.omniversity.server.user.entity.ExchangeUser;
import com.omniversity.server.user.exception.WeakPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExchangeUserService {
    private final ExchangeUserRepository exchangeUserRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ExchangeUserMapper exchangeUserMapper;
    private final UpdateExchangeUserMapper updateExchangeUserMapper;
    private final ExchangeUserResponse exchangeUserResponse;
    private final PasswordValidator passwordValidator;

    @Autowired
    public ExchangeUserService(ExchangeUserRepository exchangeUserRepository, UserService userService, PasswordEncoder passwordEncoder, ExchangeUserMapper exchangeUserMapper, UpdateExchangeUserMapper updateExchangeUserMapper, ExchangeUserResponse exchangeUserResponse, PasswordValidator passwordValidator) {
        this.exchangeUserRepository = exchangeUserRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.exchangeUserMapper = exchangeUserMapper;
        this.updateExchangeUserMapper = updateExchangeUserMapper;
        this.exchangeUserResponse = exchangeUserResponse;
        this.passwordValidator = passwordValidator;
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

    public ExchangeUser registerExchangeUser (RegistrationDto dto) throws UserAlreadyExistsException, WeakPasswordException {
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
