package com.converter;

import com.converter.cli.ConverterFacade;

/**
 * Main entry point for the file converter application.
 */
public final class Main {

    /**
     * Private constructor to prevent instantiation.
     */
    private Main() {
    }

    /**
     * Main method that starts the file converter application.
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        ConverterFacade facade = new ConverterFacade();
        int exitCode = facade.execute(args);
        System.exit(exitCode);
    }
}
