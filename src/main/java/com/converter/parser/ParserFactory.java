package com.converter.parser;

import com.converter.exception.ConversionException;

/**
 * Factory class for creating parser instances based on file extension.
 */
public final class ParserFactory {

    /**
     * Private constructor to prevent instantiation.
     */
    private ParserFactory() {
    }

    /**
     * Creates a parser for the given file path based on its extension.
     *
     * @param filePath the path to the file to parse
     * @return a parser instance appropriate for the file format
     * @throws ConversionException if the file format is not supported
     */
    public static Parser createParser(final String filePath) {
        String extension = getFileExtension(filePath);
        return createParserByExtension(extension);
    }

    /**
     * Creates a parser for the given file extension.
     *
     * @param extension the file extension (without dot)
     * @return a parser instance appropriate for the file format
     * @throws ConversionException if the file format is not supported
     */
    public static Parser createParserByExtension(final String extension) {
        return switch (extension.toLowerCase()) {
            case "json" -> new JsonParser();
            case "xml" -> new XmlParser();
            case "csv" -> new CsvParser();
            default -> throw new ConversionException(
                    "Error: Unsupported file format '." + extension + "'");
        };
    }

    /**
     * Extracts the file extension from a file path.
     *
     * @param filePath the file path
     * @return the file extension (without dot)
     * @throws ConversionException if the path is invalid or has no extension
     */
    public static String getFileExtension(final String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new ConversionException(
                    "Error: File path cannot be empty");
        }

        int lastDot = filePath.lastIndexOf('.');
        if (lastDot == -1 || lastDot == filePath.length() - 1) {
            throw new ConversionException(
                    "Error: File must have an extension");
        }

        return filePath.substring(lastDot + 1);
    }
}
