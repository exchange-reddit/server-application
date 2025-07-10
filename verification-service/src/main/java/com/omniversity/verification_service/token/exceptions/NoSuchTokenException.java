package com.omniversity.verification_service.token.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchTokenException extends RuntimeException {
    public NoSuchTokenException(String message) {
        super(message);
    }
}
