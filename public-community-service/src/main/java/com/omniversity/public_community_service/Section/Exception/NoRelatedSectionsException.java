package com.omniversity.public_community_service.Section.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoRelatedSectionsException extends RuntimeException {
    public NoRelatedSectionsException (String message) {
        super (message);
    }
}
