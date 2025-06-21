package com.omniversity.post_service.exception.custom.storage;

public class IllegalFilePathException extends FileStorageException {
    public IllegalFilePathException(String message) {
        super(message);
    }
}
