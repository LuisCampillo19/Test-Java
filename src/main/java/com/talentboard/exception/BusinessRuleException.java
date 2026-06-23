package com.talentboard.exception;

/** Thrown when a business rule is violated (e.g. applying to a closed vacancy). Maps to HTTP 409. */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
