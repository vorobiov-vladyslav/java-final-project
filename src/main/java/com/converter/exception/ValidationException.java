package com.converter.exception;

/**
 * Exception thrown when validation of input or data fails.
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs a new validation exception with the specified message.
     *
     * @param message the detail message
     */
    public ValidationException(final String message) {
        super(message);
    }

    /**
     * Constructs a validation exception with message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception
     */
    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
