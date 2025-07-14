package com.omniversity.server.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class VerificationRequestError extends RuntimeException {
    public VerificationRequestError (String message) {
        super (message);
    }
}
