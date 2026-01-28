package com.converter.exception;

/**
 * Exception thrown when a file conversion operation fails.
 */
public class ConversionException extends RuntimeException {

    /**
     * Constructs a new conversion exception with the specified message.
     *
     * @param message the detail message
     */
    public ConversionException(final String message) {
        super(message);
    }

    /**
     * Constructs a conversion exception with message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception
     */
    public ConversionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
