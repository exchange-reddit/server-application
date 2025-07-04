package com.omniversity.server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)



class PasswordValidatorTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordValidator passwordValidator;

    @Test
    void testCheckPasswordMatch_Success() {
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        assertTrue(passwordValidator.checkPasswordMatch(rawPassword, encodedPassword));
    }

    @Test
    void testCheckPasswordMatch_Failure() {
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);
        assertFalse(passwordValidator.checkPasswordMatch(rawPassword, encodedPassword));
    }

    @Test
    void testCompareNewOldPassword_SamePassword() {
        String oldPassword = "password";
        String newPassword = "password";
        assertFalse(passwordValidator.compareNewOldPassword(oldPassword, newPassword));
    }

    @Test
    void testCompareNewOldPassword_DifferentPassword() {
        String oldPassword = "password";
        String newPassword = "newPassword";
        assertTrue(passwordValidator.compareNewOldPassword(oldPassword, newPassword));
    }

    @Test
    void testValidatePasswordStrength_ValidPassword() {
        String validPassword = "Password@1";
        assertTrue(passwordValidator.validatePasswordStrength(validPassword));
    }

    @Test
    void testValidatePasswordStrength_Invalid_TooShort() {
        String invalidPassword = "Pwd@1";
        assertFalse(passwordValidator.validatePasswordStrength(invalidPassword));
    }

    @Test
    void testValidatePasswordStrength_Invalid_NoNumber() {
        String invalidPassword = "Password@";
        assertFalse(passwordValidator.validatePasswordStrength(invalidPassword));
    }

    @Test
    void testValidatePasswordStrength_Invalid_NoLowercase() {
        String invalidPassword = "PASSWORD@1";
        assertFalse(passwordValidator.validatePasswordStrength(invalidPassword));
    }

    @Test
    void testValidatePasswordStrength_Invalid_NoUppercase() {
        String invalidPassword = "password@1";
        assertFalse(passwordValidator.validatePasswordStrength(invalidPassword));
    }

    @Test
    void testValidatePasswordStrength_Invalid_NoSpecialCharacter() {
        String invalidPassword = "Password1";
        assertFalse(passwordValidator.validatePasswordStrength(invalidPassword));
    }

    @Test
    void testValidatePasswordStrength_Invalid_Whitespace() {
        String invalidPassword = "Password @1";
        assertFalse(passwordValidator.validatePasswordStrength(invalidPassword));
    }
}
