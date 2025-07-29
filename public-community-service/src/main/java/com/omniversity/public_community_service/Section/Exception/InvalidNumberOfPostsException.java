package com.omniversity.public_community_service.Section.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidNumberOfPostsException extends RuntimeException {
    public InvalidNumberOfPostsException(String message) {
        super (message);
    }
}
