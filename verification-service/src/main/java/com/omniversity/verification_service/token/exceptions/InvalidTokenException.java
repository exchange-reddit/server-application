package com.omniversity.verification_service.token.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTokenException extends Exception{
    public InvalidTokenException(String message) {
        super(message);
    }
}
