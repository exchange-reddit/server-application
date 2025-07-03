package com.omniversity.server.user.exception;

public class ChangedPasswordSameException extends RuntimeException {
    public ChangedPasswordSameException(String message) {
        super(message);
    }
}
