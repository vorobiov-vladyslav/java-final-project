package com.converter.cli;

import com.converter.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Parses command-line arguments for the file converter application.
 */
public class CliParser {

    /**
     * Command-line flag for specifying the input file.
     */
    private static final String INPUT_FLAG = "--input";

    /**
     * Command-line flag for specifying the output file.
     */
    private static final String OUTPUT_FLAG = "--output";

    /**
     * Command-line flag for requesting help.
     */
    private static final String HELP_FLAG = "--help";

    /**
     * Short command-line flag for requesting help.
     */
    private static final String SHORT_HELP_FLAG = "-h";

    /**
     * The parsed input file path.
     */
    private String inputPath;

    /**
     * The parsed output file path.
     */
    private String outputPath;

    /**
     * Flag indicating whether help was requested.
     */
    private boolean helpRequested;

    /**
     * Parses the command-line arguments.
     *
     * @param args the command-line arguments to parse
     * @return this parser instance for method chaining
     * @throws ValidationException if required arguments are missing
     */
    public CliParser parse(final String[] args) {
        Map<String, String> argsMap = parseArgsToMap(args);

        helpRequested = argsMap.containsKey(HELP_FLAG)
                || argsMap.containsKey(SHORT_HELP_FLAG);

        if (helpRequested) {
            return this;
        }

        inputPath = argsMap.get(INPUT_FLAG);
        outputPath = argsMap.get(OUTPUT_FLAG);

        validate();

        return this;
    }

    /**
     * Parses command-line arguments into a map.
     *
     * @param args the command-line arguments
     * @return a map of argument flags to their values
     */
    private Map<String, String> parseArgsToMap(final String[] args) {
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

    /**
     * Validates that required arguments are present.
     *
     * @throws ValidationException if required arguments are missing
     */
    private void validate() {
        if (inputPath == null || inputPath.trim().isEmpty()) {
            throw new ValidationException(
                    "Error: Missing required argument --input");
        }
        if (outputPath == null || outputPath.trim().isEmpty()) {
            throw new ValidationException(
                    "Error: Missing required argument --output");
        }
    }

    /**
     * Returns the parsed input file path.
     *
     * @return the input file path
     */
    public String getInputPath() {
        return inputPath;
    }

    /**
     * Returns the parsed output file path.
     *
     * @return the output file path
     */
    public String getOutputPath() {
        return outputPath;
    }

    /**
     * Returns whether help was requested.
     *
     * @return true if help was requested, false otherwise
     */
    public boolean isHelpRequested() {
        return helpRequested;
    }

    /**
     * Returns the help message for the application.
     *
     * @return the help message string
     */
    public static String getHelpMessage() {
        return "File Converter - Convert files between JSON, XML, "
                + "and CSV formats\n\n"
                + "Usage:\n"
                + "  java -jar file-converter.jar "
                + "--input <input-file> --output <output-file>\n\n"
                + "Options:\n"
                + "  --input   Path to the input file (required)\n"
                + "  --output  Path to the output file (required)\n"
                + "  --help    Show this help message\n\n"
                + "Supported formats:\n"
                + "  - JSON (.json)\n"
                + "  - XML (.xml)\n"
                + "  - CSV (.csv)\n\n"
                + "Examples:\n"
                + "  java -jar file-converter.jar "
                + "--input data.json --output data.csv\n"
                + "  java -jar file-converter.jar "
                + "--input data.xml --output data.json\n"
                + "  java -jar file-converter.jar "
                + "--input data.csv --output data.xml\n\n"
                + "Note: Only flat data structures are supported. "
                + "Nested objects/arrays will\ncause an error.\n";
    }
}
