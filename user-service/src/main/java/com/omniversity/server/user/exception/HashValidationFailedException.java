package com.omniversity.server.user.exception;

public class HashValidationFailedException extends Exception {
    public HashValidationFailedException (String message) {
        super (message);
    }
}
