package com.omniversity.post_service.exception.custom.storage;

// Specific error types
public class StorageInitializationException extends StorageException {
    public StorageInitializationException(String message) { super(message); }
    public StorageInitializationException(String message, Throwable cause) { super(message, cause); }
}
