package com.omniversity.post_service.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long postId) {
        super("Post with ID " + postId + " not found.");
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}