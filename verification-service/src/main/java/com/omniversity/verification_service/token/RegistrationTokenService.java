package com.omniversity.verification_service.token;

import com.omniversity.verification_service.service.DefaultAccountVerificationContext;
import com.omniversity.verification_service.service.EmailService;
import com.omniversity.verification_service.token.entity.RegistrationToken;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.omniversity.verification_service.token.exceptions.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;

public class RegistrationTokenService {

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(5);
    private int tokenValiditySeconds = 900;
    @Autowired
    private RegistrationTokenRepository registrationTokenRepository;
    private EmailService emailService;

    @Autowired
    public RegistrationTokenService(int tokenValiditySeconds, RegistrationTokenRepository registrationTokenRepository, EmailService emailService) {
        this.tokenValiditySeconds = tokenValiditySeconds;
        this.registrationTokenRepository = registrationTokenRepository;
        this.emailService = emailService;
    }

    /**
     * About: Generate registration token for email (private, home university, and exchange university) verification.
     * Returns: Registration token (Verification Code) for the designated user
     */
    public RegistrationToken createRegistrationToken(String email, int verificationType) {
        String tokenValue = DEFAULT_TOKEN_GENERATOR.generateKey().toString();
        RegistrationToken registrationToken = new RegistrationToken();
        registrationToken.setToken(tokenValue);
        registrationToken.setExpiryDate(LocalDateTime.now().plusSeconds(tokenValiditySeconds));
        registrationToken.setVerificationType(verificationType);
        registrationToken.setEmail(email);
        this.saveRegistrationToken(registrationToken);

        return registrationToken;
    }

    /**
     * About: Save registration token to the token table
     * Params: Registration token to be saved
     */
    public void saveRegistrationToken(RegistrationToken registrationToken) {
        registrationTokenRepository.save(registrationToken);
    }

    public void sendRegistrationToken(String name, String emailAddress, int option, RegistrationToken registrationToken) {
        DefaultAccountVerificationContext emailContext = new DefaultAccountVerificationContext();
        emailContext.init(name);
        emailContext.setTo(emailAddress);
        emailContext.setToken(registrationToken.getToken());

        try {
            emailService.sendMail(emailContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * About: Find registration token by the token value and return the token
     * Params: String value of the registration token
     * Returns: Registration token
     */
    public RegistrationToken findByToken(String token) {
        return registrationTokenRepository.findByToken(token);
    }

    public Boolean verifyToken(String verificationCode, String email, int verificationType) throws InvalidTokenException {
        RegistrationToken token = registrationTokenRepository.findByEmail(email);
        LocalDateTime now = LocalDateTime.now();

        if (!Objects.equals(token.getToken(), verificationCode) || verificationType != token.getVerificationType()) {
            throw new InvalidTokenException("Provided token does not match either the token value or verification type");
        }

        else if (now.isAfter(token.getExpiryDate())) {
            throw new InvalidTokenException("Provided token has been expired");
        }

        else {
            return true;
        }
    }

    /**
     * About: Remove verified registration token from db
     * Params: String value of the registration token
     */
    public void removeToken(RegistrationToken token) {
        registrationTokenRepository.delete(token);
    }

}
