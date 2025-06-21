package com.omniversity.post_service.exception.custom.storage;

public class InvalidFileException extends StorageException {
    public InvalidFileException(String message) { super(message); }
    public InvalidFileException(String message, Throwable cause) { super(message, cause); }
}
