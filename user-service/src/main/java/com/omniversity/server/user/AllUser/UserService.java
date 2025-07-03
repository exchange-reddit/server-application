package com.omniversity.server.user.AllUser;

import com.omniversity.server.user.ExchangeUser.ExchangeUserRepository;
import com.omniversity.server.user.exception.ChangedPasswordSameException;
import com.omniversity.server.user.exception.NoSuchUserException;
import com.omniversity.server.user.exception.PassedGracePeriodException;
import com.omniversity.server.user.exception.WrongPasswordException;
import com.omniversity.server.log.dto.accountDeleteLogDto;
import com.omniversity.server.log.dto.pwChangeLogDto;
import com.omniversity.server.service.PasswordValidator;
import com.omniversity.server.user.AllUser.dto.request.ChangeNationalityDto;
import com.omniversity.server.user.AllUser.dto.request.ChangePasswordDto;
import com.omniversity.server.user.AllUser.dto.request.DeleteUserDto;
import com.omniversity.server.user.entity.AbstractUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * TODO
 * Add email verification step prior to registration
 */
@Service
public class UserService {
    private final AbstractUserRepository abstractUserRepository;
    private final ExchangeUserRepository exchangeUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;

    @Autowired
    public UserService(AbstractUserRepository abstractUserRepository, ExchangeUserRepository exchangeUserRepository, PasswordEncoder passwordEncoder, PasswordValidator passwordValidator) {
        this.abstractUserRepository = abstractUserRepository;
        this.exchangeUserRepository = exchangeUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
    }

    public Boolean checkUserNameTaken(String userName) {
        return abstractUserRepository.findByUserName(userName).isPresent();
    }

    public boolean checkEmailRegistered (String email, int option) {
        switch (option) {
            case (1):
                return abstractUserRepository.findByPrivateEmail(email).isPresent();
            case (2):
                return exchangeUserRepository.findByExchangeEmail(email).isPresent();
            case (3):
                return abstractUserRepository.findByHomeEmail(email).isPresent();
            default:
                return false;
        }
    }

    public AbstractUser getUser (Long id) throws NoSuchUserException {
        Optional<AbstractUser> optionalAbstractUser = abstractUserRepository.findById(id);

        if (optionalAbstractUser.isEmpty()) {
            throw new NoSuchUserException((String.format("User ID %d does not exist in our system.", id)));
        }

        return optionalAbstractUser.get();
    }

    public pwChangeLogDto changePassword(ChangePasswordDto changePasswordDto, Long id) throws RuntimeException {
        try {
            AbstractUser user = getUser(id);
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

            // Calculate and save the hash value of the new provided password
            user.setPasswordHash(passwordEncoder.encode(changePasswordDto.newPassword()));
            // Save the user to the database
            abstractUserRepository.save(user);

            // Send the user object with the previous password hash
            user.setPasswordHash(prevHash);

            // Return the pwChangeLog in a form of dto
            return new pwChangeLogDto(String.valueOf(id), prevHash);

        } catch (Exception e) {
            throw e;
        }
    }

    // A dedicated method to assist users to change their nationality (Users are allowed to change their nationality for once during the grace period of 2 weeks)
    public void changeNationality(Long id, ChangeNationalityDto dto) throws PassedGracePeriodException {
        // Get requesting user through their id
        AbstractUser user = getUser(id);
        // Retrieve the server date of the day of operation (Server Time)
        LocalDate today = LocalDate.now();
        // Calculate the days between the registration date and the date of request
        long daysBetween = ChronoUnit.DAYS.between(today, user.getRegistrationDate());

        // Check if the grace period has passed or not
        if (daysBetween > 14) {
            throw new PassedGracePeriodException(String.format("%d days have passed since your registration to our service. The grace period to change your nationality is 2 weeks.", daysBetween));
        }

        user.setNationality(dto.nationality());
        abstractUserRepository.save(user);
    }


    public accountDeleteLogDto deleteUser(DeleteUserDto deleteUserDto) throws WrongPasswordException {
        // Retrieve the user that will be deleted
        AbstractUser user = getUser(deleteUserDto.userId());

        if (!passwordValidator.checkPasswordMatch(deleteUserDto.password(), user.getPasswordHash())) {
            throw new WrongPasswordException("The provided previous password does not match.");
        }

        abstractUserRepository.deleteById(user.getId());

        return new accountDeleteLogDto(String.valueOf(deleteUserDto.userId()), "Delete Account");

    }

}
