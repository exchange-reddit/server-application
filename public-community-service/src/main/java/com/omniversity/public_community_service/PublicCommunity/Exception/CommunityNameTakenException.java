package com.omniversity.public_community_service.PublicCommunity.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CommunityNameTakenException extends RuntimeException {
    public CommunityNameTakenException (String message) {
        super (message);
    }
}
