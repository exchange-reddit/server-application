package com.omniversity.server.service;

// This class handles the password strength validation

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    private PasswordEncoder passwordEncoder;

    public PasswordValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean checkPasswordMatch(String oldPassword, String passwordHash) {
        if (!passwordEncoder.matches(oldPassword, passwordHash)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean compareNewOldPassword (String oldPassword, String newPassword) {
        if (Objects.equals(oldPassword, newPassword)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validatePasswordStrength(String password) {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            return false;
        } else {
            return true;
        }

    }
}
