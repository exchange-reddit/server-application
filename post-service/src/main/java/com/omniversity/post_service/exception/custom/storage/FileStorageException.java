package com.omniversity.post_service.exception.custom.storage;

public class FileStorageException extends StorageException {
    public FileStorageException(String message) { super(message); }
    public FileStorageException(String message, Throwable cause) { super(message, cause); }
}
