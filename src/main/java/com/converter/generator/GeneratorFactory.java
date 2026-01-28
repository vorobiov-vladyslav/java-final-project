package com.converter.generator;

import com.converter.exception.ConversionException;

public class GeneratorFactory {

    private GeneratorFactory() {
    }

    public static Generator createGenerator(String filePath) {
        String extension = getFileExtension(filePath);
        return createGeneratorByExtension(extension);
    }

    public static Generator createGeneratorByExtension(String extension) {
        return switch (extension.toLowerCase()) {
            case "json" -> new JsonGenerator();
            case "xml" -> new XmlGenerator();
            case "csv" -> new CsvGenerator();
            default -> throw new ConversionException("Error: Unsupported file format '." + extension + "'");
        };
    }

    public static String getFileExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new ConversionException("Error: File path cannot be empty");
        }

        int lastDot = filePath.lastIndexOf('.');
        if (lastDot == -1 || lastDot == filePath.length() - 1) {
            throw new ConversionException("Error: File must have an extension");
        }

        return filePath.substring(lastDot + 1);
    }
}
