package com.converter.cli;

import com.converter.converter.FileConverter;
import com.converter.exception.ConversionException;
import com.converter.exception.ParseException;
import com.converter.exception.ValidationException;

public class ConverterFacade {

    public void convert(String inputPath, String outputPath) {
        FileConverter converter = new FileConverter(inputPath, outputPath);
        converter.convert(inputPath, outputPath);
    }

    public int execute(String[] args) {
        try {
            CliParser cliParser = new CliParser().parse(args);

            if (cliParser.isHelpRequested()) {
                System.out.println(CliParser.getHelpMessage());
                return 0;
            }

            String inputPath = cliParser.getInputPath();
            String outputPath = cliParser.getOutputPath();

            convert(inputPath, outputPath);

            System.out.println("Successfully converted '" + inputPath + "' to '" + outputPath + "'");
            return 0;

        } catch (ValidationException | ParseException | ConversionException e) {
            System.err.println(e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("Error: An unexpected error occurred: " + e.getMessage());
            return 1;
        }
    }
}
