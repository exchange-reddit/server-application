package com.omniversity.post_service.exception.custom.storage;

public class InvalidFileNameException extends FileStorageException {
    public InvalidFileNameException(String message) { super(message); }
    public InvalidFileNameException(String message, Throwable cause) { super(message, cause); }
}
