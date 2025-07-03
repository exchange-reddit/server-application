package com.omniversity.server.user.ProspectiveUser;

import com.omniversity.server.user.exception.NoSuchUserException;
import com.omniversity.server.service.PasswordValidator;
import com.omniversity.server.user.ProspectiveUser.dto.request.DesiredUniversityUpdateDto;
import com.omniversity.server.user.ProspectiveUser.dto.response.ProspectiveUserResponseNoPasswordDto;
import com.omniversity.server.user.ProspectiveUser.mapper.response.ProspectiveUserResponse;
import com.omniversity.server.user.exception.UserAlreadyExistsException;
import com.omniversity.server.user.ProspectiveUser.dto.request.ProspectiveUserRegistrationDto;
import com.omniversity.server.user.ProspectiveUser.mapper.functional.ProspectiveUserMapper;
import com.omniversity.server.user.AllUser.UserService;
import com.omniversity.server.user.entity.ProspectiveUser;
import com.omniversity.server.user.exception.WeakPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProspectiveUserService {
    private final ProspectiveUserRepository prospectiveUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final ProspectiveUserMapper prospectiveUserMapper;
    private final ProspectiveUserResponse prospectiveUserResponse;
    private final UserService userService;

    @Autowired
    public ProspectiveUserService (ProspectiveUserRepository prospectiveUserRepository, PasswordEncoder passwordEncoder, PasswordValidator passwordValidator, ProspectiveUserMapper prospectiveUserMapper, ProspectiveUserResponse prospectiveUserResponse, UserService userService) {
        this.prospectiveUserRepository = prospectiveUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
        this.prospectiveUserMapper = prospectiveUserMapper;
        this.prospectiveUserResponse = prospectiveUserResponse;
        this.userService = userService;
    }

    public ProspectiveUser registerProspectiveUser(ProspectiveUserRegistrationDto dto) throws UserAlreadyExistsException, WeakPasswordException {
        if (userService.checkEmailRegistered(dto.homeEmail(), 3)) {
            throw new UserAlreadyExistsException(String.format("You have already created an account using this email: %s", dto.homeEmail()));
        }

        if (userService.checkUserNameTaken(dto.userName())) {
            throw new UserAlreadyExistsException(String.format("The username %s has already been taken by an another user", dto.userName()));
        }

        ProspectiveUser user = prospectiveUserMapper.toEntity(dto);

        if (!passwordValidator.validatePasswordStrength(dto.password())) {
            throw new WeakPasswordException("The provided password does not comply to the security requirement.");
        }

        user.setPasswordHash(passwordEncoder.encode(dto.password()));

        prospectiveUserRepository.save(user);

        return user;
    }

    public ProspectiveUserResponseNoPasswordDto updateDesiredExchangeUniversity(DesiredUniversityUpdateDto dto, Long id) throws NoSuchUserException {
        Optional<ProspectiveUser> optionalUser = Optional.of(prospectiveUserRepository.findById(id)).orElseThrow(
                () -> new NoSuchUserException(String.format("No such user exists with the following id: %d", id))
        );

        ProspectiveUser user = optionalUser.get();

        user.setDesiredUniversity(dto.desiredUniversity());
        prospectiveUserRepository.save(user);

        return prospectiveUserResponse.toResponseProspectiveDto(user);
    }
}
