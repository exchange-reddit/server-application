package com.omniversity.post_service.exception.custom.storage;

public class UnauthorizedStorageAccessException extends StorageException {
    public UnauthorizedStorageAccessException(String message) { super(message); }
    public UnauthorizedStorageAccessException(String message, Throwable cause) { super(message, cause); }
}
