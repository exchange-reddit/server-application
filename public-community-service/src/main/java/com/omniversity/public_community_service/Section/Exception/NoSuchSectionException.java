package com.omniversity.public_community_service.Section.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchSectionException extends RuntimeException {
    public NoSuchSectionException(String message) {
        super(message);
    }
}
