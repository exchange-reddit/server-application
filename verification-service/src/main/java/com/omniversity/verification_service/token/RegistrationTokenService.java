package com.omniversity.verification_service.token;

import com.omniversity.verification_service.service.DefaultAccountVerificationContext;
import com.omniversity.verification_service.service.EmailService;
import com.omniversity.verification_service.token.dto.RegistrationDto;
import com.omniversity.verification_service.token.entity.RegistrationToken;
import com.omniversity.verification_service.token.dto.VerificationDto;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.omniversity.verification_service.token.exceptions.InvalidTokenException;
import com.omniversity.verification_service.token.exceptions.NoSuchTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class RegistrationTokenService {

    // Default method to generate random verification key (Line 22 to 24)
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int MAX = 99999;
    private static final int MIN = 10000;

    // Define the validity period of the registration token (Set as 15 min)
    private static final int VALIDITY = 900;
    @Autowired
    private RegistrationTokenRepository registrationTokenRepository;
    private EmailService emailService;
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    public RegistrationTokenService(RegistrationTokenRepository registrationTokenRepository, EmailService emailService, TemplateEngine templateEngine) {
        this.registrationTokenRepository = registrationTokenRepository;
        this.emailService = emailService;
        this.templateEngine = templateEngine;
    }

    /**
     * About: Generate registration token for email (private, home university, and exchange university) verification.
     * Returns: Registration token (Verification Code) for the designated user
     */
    public RegistrationToken createRegistrationToken(RegistrationDto registrationDto) {
        // Create random validation code first (Currently set to be a 5 digit key)
        String randomCode = String.valueOf(secureRandom.nextInt(MAX-MIN) + MIN);

        // Instantiate a new registrationToken
        // Thought about using the automatic mapping for code readability but as half of the attributes cannot be mapped automatically, doing this manually
        RegistrationToken registrationToken = new RegistrationToken();
        // Sets the verification code of the registration token
        registrationToken.setCode(randomCode);
        // Defines the validity period of the registration token (Change this through VALIDITY)
        registrationToken.setExpiryDate(LocalDateTime.now().plusSeconds(VALIDITY));
        // Defines the email type that it verifies
        registrationToken.setVerificationType(registrationDto.verificationType());
        // Set the email that this token is verifying
        registrationToken.setEmail(registrationDto.email());
        // Save the token to DB
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

    /**
     * About: Check if there are any previous verification requests. If any, delete previous token.
     * Params: Email address to receive the code
     */
    public void checkDuplicate(String email) {
        Optional<RegistrationToken> optionalToken = registrationTokenRepository.findByEmail(email);
        optionalToken.ifPresent(this::removeToken);
    }

    public void sendRegistrationToken(String name, String emailAddress, RegistrationToken registrationToken) {
        // Instantiate a new emailContext object
        DefaultAccountVerificationContext emailContext = new DefaultAccountVerificationContext();
        // Add the name to the object
        emailContext.init(name);
        // Define the receiver of the email
        emailContext.setTo(emailAddress);
        // Add OTP code to the email
        emailContext.setToken(registrationToken.getCode());

        // Using Thyme Leaf library to add name and OTP code to the email template
        Context thymeLeafContext = new Context();
        thymeLeafContext.setVariable("token", registrationToken.getCode());
        thymeLeafContext.setVariable("name", name);

        // Process the email template using the set variables
        String emailContent =  templateEngine.process("mailing/email_verification_en", thymeLeafContext);

        // Add content to the emailContext object
        emailContext.setEmail(emailContent);

        try {
            // Send the verification email to the receiver
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
    public RegistrationToken findByToken(String code) throws NoSuchTokenException {
        RegistrationToken token = registrationTokenRepository.findByCode(code)
                .orElseThrow(() ->
                {
                    throw new NoSuchTokenException("No registration token was found with the following id");
                });

        return token;
    }

    public void verifyToken(VerificationDto verificationDto) throws InvalidTokenException, NoSuchTokenException {
        try {
            // Find the token by using the provided email
            RegistrationToken token = registrationTokenRepository.findByEmail(verificationDto.email())
                    .orElseThrow(() ->
                    {
                        throw new NoSuchTokenException(String.format("No registration token was assigned to the following email: %s", verificationDto.email()));
                    });

            // Variable to see if the token is expired or not
            LocalDateTime now = LocalDateTime.now();

            // If the user provided token and verification type equals to what is in DB, pass.
            // If failed, throw an error
            if (!Objects.equals(token.getCode(), verificationDto.code()) || token.getVerificationType() != verificationDto.verificationType()) {
                throw new InvalidTokenException("Provided token does not match either the token value or verification type");
            }

            // If the token passed the expiration date, throw an error
            else if (now.isAfter(token.getExpiryDate())) {
                throw new InvalidTokenException("Provided token has been expired");
            }

            else {
                // Remove token from DB
                removeToken(token);
            }
        } catch (NoSuchTokenException | InvalidTokenException e) {
            // Re-throw specific exceptions so the caller can handle them.
            throw e;
        } catch (Exception e) {
            throw e;
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
