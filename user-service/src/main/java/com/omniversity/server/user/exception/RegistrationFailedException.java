package com.omniversity.server.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegistrationFailedException extends RuntimeException {
    public RegistrationFailedException (String message) {
        super (message);
    }
}
