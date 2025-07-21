package com.omniversity.public_community_service.PublicCommunity.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommunityNotFoundException extends RuntimeException {
    public CommunityNotFoundException (String message) {
        super (message);
    }
}
