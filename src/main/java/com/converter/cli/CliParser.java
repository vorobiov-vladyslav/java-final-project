package com.converter.cli;

import com.converter.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

public class CliParser {

    private static final String INPUT_FLAG = "--input";
    private static final String OUTPUT_FLAG = "--output";
    private static final String HELP_FLAG = "--help";
    private static final String SHORT_HELP_FLAG = "-h";

    private String inputPath;
    private String outputPath;
    private boolean helpRequested;

    public CliParser parse(String[] args) {
        Map<String, String> argsMap = parseArgsToMap(args);

        helpRequested = argsMap.containsKey(HELP_FLAG) || argsMap.containsKey(SHORT_HELP_FLAG);

        if (helpRequested) {
            return this;
        }

        inputPath = argsMap.get(INPUT_FLAG);
        outputPath = argsMap.get(OUTPUT_FLAG);

        validate();

        return this;
    }

    private Map<String, String> parseArgsToMap(String[] args) {
        Map<String, String> result = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equals(HELP_FLAG) || arg.equals(SHORT_HELP_FLAG)) {
                result.put(arg, "true");
            } else if (arg.startsWith("--") && i + 1 < args.length) {
                result.put(arg, args[++i]);
            }
        }

        return result;
    }

    private void validate() {
        if (inputPath == null || inputPath.trim().isEmpty()) {
            throw new ValidationException("Error: Missing required argument --input");
        }
        if (outputPath == null || outputPath.trim().isEmpty()) {
            throw new ValidationException("Error: Missing required argument --output");
        }
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public boolean isHelpRequested() {
        return helpRequested;
    }

    public static String getHelpMessage() {
        return """
                File Converter - Convert files between JSON, XML, and CSV formats

                Usage:
                  java -jar file-converter.jar --input <input-file> --output <output-file>

                Options:
                  --input   Path to the input file (required)
                  --output  Path to the output file (required)
                  --help    Show this help message

                Supported formats:
                  - JSON (.json)
                  - XML (.xml)
                  - CSV (.csv)

                Examples:
                  java -jar file-converter.jar --input data.json --output data.csv
                  java -jar file-converter.jar --input data.xml --output data.json
                  java -jar file-converter.jar --input data.csv --output data.xml

                Note: Only flat data structures are supported. Nested objects/arrays will
                cause an error.
                """;
    }
}
