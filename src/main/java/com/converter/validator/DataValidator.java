package com.converter.validator;

import com.converter.exception.ValidationException;
import com.converter.model.DataRecord;

import java.io.File;
import java.util.List;

/**
 * Utility class for validating input files, output files, and data records.
 */
public final class DataValidator {

    /**
     * Private constructor to prevent instantiation.
     */
    private DataValidator() {
    }

    /**
     * Validates that the input file exists and is readable.
     *
     * @param filePath the path to the input file
     * @throws ValidationException if the file is invalid
     */
    public static void validateInputFile(final String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new ValidationException(
                    "Error: Input file path cannot be empty");
        }

        File file = new File(filePath);
        if (!file.exists()) {
            throw new ValidationException(
                    "Error: Input file '" + filePath + "' not found");
        }
        if (!file.isFile()) {
            throw new ValidationException(
                    "Error: '" + filePath + "' is not a file");
        }
        if (!file.canRead()) {
            throw new ValidationException(
                    "Error: Cannot read file '" + filePath + "'");
        }
    }

    /**
     * Validates that the output file path is writable.
     *
     * @param filePath the path to the output file
     * @throws ValidationException if the path is invalid or not writable
     */
    public static void validateOutputFile(final String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new ValidationException(
                    "Error: Output file path cannot be empty");
        }

        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && parentDir.exists() && !parentDir.canWrite()) {
            throw new ValidationException(
                    "Error: Cannot write to directory '"
                            + parentDir.getPath() + "'");
        }

        if (file.exists() && !file.canWrite()) {
            throw new ValidationException(
                    "Error: Cannot overwrite file '" + filePath + "'");
        }
    }

    /**
     * Validates that the data records are not null.
     *
     * @param records the list of data records to validate
     * @throws ValidationException if records is null
     */
    public static void validateData(final List<DataRecord> records) {
        if (records == null) {
            throw new ValidationException("Error: Data cannot be null");
        }
    }

    /**
     * Validates that the file has a supported extension.
     *
     * @param filePath  the path to the file
     * @param supported the list of supported extensions
     * @throws ValidationException if the extension is not supported
     */
    public static void validateFileExtension(
            final String filePath, final String... supported) {
        String extension = getFileExtension(filePath);
        for (String ext : supported) {
            if (ext.equalsIgnoreCase(extension)) {
                return;
            }
        }
        throw new ValidationException(
                "Error: Unsupported file format '." + extension + "'");
    }

    /**
     * Extracts the file extension from a file path.
     *
     * @param filePath the file path
     * @return the file extension (without dot)
     * @throws ValidationException if the path is invalid or has no extension
     */
    private static String getFileExtension(final String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new ValidationException(
                    "Error: File path cannot be empty");
        }

        int lastDot = filePath.lastIndexOf('.');
        if (lastDot == -1 || lastDot == filePath.length() - 1) {
            throw new ValidationException(
                    "Error: File must have an extension");
        }

        return filePath.substring(lastDot + 1);
    }
}
