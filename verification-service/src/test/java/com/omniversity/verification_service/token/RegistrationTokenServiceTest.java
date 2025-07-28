package com.omniversity.verification_service.token;

import com.omniversity.verification_service.service.DefaultAccountVerificationContext;
import com.omniversity.verification_service.service.EmailService;
import com.omniversity.verification_service.token.dto.RegistrationDto;
import com.omniversity.verification_service.token.dto.VerificationDto;
import com.omniversity.verification_service.token.entity.RegistrationToken;
import com.omniversity.verification_service.token.exceptions.InvalidTokenException;
import com.omniversity.verification_service.token.exceptions.NoSuchTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationTokenServiceTest {
    @Mock
    private RegistrationTokenRepository registrationTokenRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private RegistrationTokenService registrationTokenService;

    private RegistrationToken registrationToken;

    @BeforeEach
    void setUp() {
        registrationToken = new RegistrationToken();
        registrationToken.setId(1L);
        registrationToken.setCode("RandomCode");
        registrationToken.setExpiryDate(LocalDateTime.now());
        registrationToken.setEmail("test@email.com");
        registrationToken.setVerificationType(1);
    }

    @Test
    void createRegistrationToken_Success() {
        // Given
        RegistrationDto registrationDto = new RegistrationDto("testUser", "test@email.com", 1);

        // When
        RegistrationToken createdToken = registrationTokenService.createRegistrationToken(registrationDto);

        // Then
        assertNotNull(createdToken);
        assertNotNull(createdToken.getCode());
        assertEquals(5, createdToken.getCode().length());
        assertEquals(registrationDto.email(), createdToken.getEmail());
        assertEquals(registrationDto.verificationType(), createdToken.getVerificationType());
        assertTrue(createdToken.getExpiryDate().isAfter(LocalDateTime.now()));
        verify(registrationTokenRepository, times(1)).save(any(RegistrationToken.class));
    }

    @Test
    void saveRegistrationToken_Success() {
        // When
        registrationTokenService.saveRegistrationToken(registrationToken);

        // Then
        verify(registrationTokenRepository, times(1)).save(registrationToken);
    }

    @Test
    void checkDuplicate_TokenExists_RemovesToken() {
        // Given
        String email = "test@email.com";
        when(registrationTokenRepository.findByEmail(email)).thenReturn(Optional.of(registrationToken));

        // When
        registrationTokenService.checkDuplicate(email);

        // Then
        verify(registrationTokenRepository, times(1)).findByEmail(email);
        verify(registrationTokenRepository, times(1)).delete(registrationToken);
    }

    @Test
    void sendRegistrationToken_Success() throws Exception {
        // Given
        String name = "testUser";
        String email = "test@email.com";
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("emailContent");

        // When
        registrationTokenService.sendRegistrationToken(name, email, registrationToken);

        // Then
        verify(emailService, times(1)).sendMail(any(DefaultAccountVerificationContext.class));
    }

    @Test
    void findByToken_Success() throws NoSuchTokenException {
        // Given
        String tokenCode = "RandomCode";
        when(registrationTokenRepository.findByCode(tokenCode)).thenReturn(Optional.of(registrationToken));

        // When
        RegistrationToken foundToken = registrationTokenService.findByToken(tokenCode);

        // Then
        assertNotNull(foundToken);
        assertEquals(tokenCode, foundToken.getCode());
        verify(registrationTokenRepository, times(1)).findByCode(tokenCode);
    }

    @Test
    void findByToken_TokenNotFound_ThrowsNoSuchTokenException() {
        // Given
        String tokenCode = "NonExistentCode";
        when(registrationTokenRepository.findByCode(tokenCode)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchTokenException.class, () -> {
            registrationTokenService.findByToken(tokenCode);
        });
        verify(registrationTokenRepository, times(1)).findByCode(tokenCode);
    }

    @Test
    void verifyToken_Success() throws InvalidTokenException, NoSuchTokenException {
        // Given
        VerificationDto verificationDto = new VerificationDto("RandomCode", "test@email.com", 1);
        registrationToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Ensure token is not expired
        when(registrationTokenRepository.findByEmail(verificationDto.email())).thenReturn(Optional.of(registrationToken));

        registrationTokenService.verifyToken(verificationDto);


        verify(registrationTokenRepository, times(1)).findByEmail(verificationDto.email());
        verify(registrationTokenRepository, times(1)).delete(registrationToken);
    }

    @Test
    void verifyToken_TokenNotFound_ReturnsFalse() throws InvalidTokenException, NoSuchTokenException {
        // Given
        VerificationDto verificationDto = new VerificationDto("RandomCode", "nonexistent@email.com", 1);
        when(registrationTokenRepository.findByEmail(verificationDto.email())).thenReturn(Optional.empty());

        registrationTokenService.verifyToken(verificationDto);

        verify(registrationTokenRepository, times(1)).findByEmail(verificationDto.email());
        verify(registrationTokenRepository, times(0)).delete(any(RegistrationToken.class));
    }

    @Test
    void removeToken_Success() {
        // When
        registrationTokenService.removeToken(registrationToken);

        // Then
        verify(registrationTokenRepository, times(1)).delete(registrationToken);
    }
}
