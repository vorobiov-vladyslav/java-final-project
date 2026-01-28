package com.converter.parser;

import com.converter.exception.ConversionException;

public class ParserFactory {

    private ParserFactory() {
    }

    public static Parser createParser(String filePath) {
        String extension = getFileExtension(filePath);
        return createParserByExtension(extension);
    }

    public static Parser createParserByExtension(String extension) {
        return switch (extension.toLowerCase()) {
            case "json" -> new JsonParser();
            case "xml" -> new XmlParser();
            case "csv" -> new CsvParser();
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
