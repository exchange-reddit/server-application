package com.omniversity.post_service.exception.custom.storage;

public class FileDeletionException extends StorageException {
    public FileDeletionException(String message) { super(message); }
    public FileDeletionException(String message, Throwable cause) { super(message, cause); }
}
