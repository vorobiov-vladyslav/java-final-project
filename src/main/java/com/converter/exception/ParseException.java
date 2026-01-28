package com.converter.exception;

/**
 * Exception thrown when parsing an input file fails.
 */
public class ParseException extends RuntimeException {

    /**
     * Constructs a new parse exception with the specified message.
     *
     * @param message the detail message
     */
    public ParseException(final String message) {
        super(message);
    }

    /**
     * Constructs a new parse exception with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception
     */
    public ParseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
