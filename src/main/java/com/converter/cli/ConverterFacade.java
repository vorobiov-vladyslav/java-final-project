package com.converter.cli;

import com.converter.converter.FileConverter;
import com.converter.exception.ConversionException;
import com.converter.exception.ParseException;
import com.converter.exception.ValidationException;

/**
 * Facade class providing a simplified interface for file conversions.
 */
public class ConverterFacade {

    /**
     * Converts a file from one format to another.
     *
     * @param inputPath  the path to the input file
     * @param outputPath the path to the output file
     */
    public void convert(final String inputPath, final String outputPath) {
        FileConverter converter = new FileConverter(inputPath, outputPath);
        converter.convert(inputPath, outputPath);
    }

    /**
     * Executes the file converter with the given command-line arguments.
     *
     * @param args the command-line arguments
     * @return 0 on success, 1 on error
     */
    public int execute(final String[] args) {
        try {
            CliParser cliParser = new CliParser().parse(args);

            if (cliParser.isHelpRequested()) {
                System.out.println(CliParser.getHelpMessage());
                return 0;
            }

            String inputPath = cliParser.getInputPath();
            String outputPath = cliParser.getOutputPath();

            convert(inputPath, outputPath);

            System.out.println("Successfully converted '"
                    + inputPath + "' to '" + outputPath + "'");
            return 0;

        } catch (ValidationException | ParseException | ConversionException e) {
            System.err.println(e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("Error: An unexpected error occurred: "
                    + e.getMessage());
            return 1;
        }
    }
}
