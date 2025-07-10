package com.omniversity.server.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PassedGracePeriodException extends RuntimeException {
    public PassedGracePeriodException(String message) {
        super(message);
    }
}
