package com.omniversity.server.exception;

public class ChangedPasswordSameException extends RuntimeException {
    public ChangedPasswordSameException(String message) {
        super(message);
    }
}
