package com.talentboard.exception;

/** Thrown when a user tries to access data they are not allowed to. Maps to HTTP 403. */
public class AccessDeniedAppException extends RuntimeException {
    public AccessDeniedAppException(String message) {
        super(message);
    }
}
