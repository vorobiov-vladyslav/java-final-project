package com.converter.generator;

import com.converter.exception.ConversionException;

/**
 * Factory class for creating generator instances based on file extension.
 */
public final class GeneratorFactory {

    /**
     * Private constructor to prevent instantiation.
     */
    private GeneratorFactory() {
    }

    /**
     * Creates a generator for the given file path based on its extension.
     *
     * @param filePath the path to the output file
     * @return a generator instance appropriate for the file format
     * @throws ConversionException if the file format is not supported
     */
    public static Generator createGenerator(final String filePath) {
        String extension = getFileExtension(filePath);
        return createGeneratorByExtension(extension);
    }

    /**
     * Creates a generator for the given file extension.
     *
     * @param extension the file extension (without dot)
     * @return a generator instance appropriate for the file format
     * @throws ConversionException if the file format is not supported
     */
    public static Generator createGeneratorByExtension(final String extension) {
        return switch (extension.toLowerCase()) {
            case "json" -> new JsonGenerator();
            case "xml" -> new XmlGenerator();
            case "csv" -> new CsvGenerator();
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
