package com.converter;

import com.converter.cli.ConverterFacade;

public class Main {

    public static void main(String[] args) {
        ConverterFacade facade = new ConverterFacade();
        int exitCode = facade.execute(args);
        System.exit(exitCode);
    }
}
