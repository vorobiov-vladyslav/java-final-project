package com.converter.validator;

import com.converter.exception.ValidationException;
import com.converter.model.DataRecord;

import java.io.File;
import java.util.List;

public class DataValidator {

    private DataValidator() {
    }

    public static void validateInputFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new ValidationException("Error: Input file path cannot be empty");
        }

        File file = new File(filePath);
        if (!file.exists()) {
            throw new ValidationException("Error: Input file '" + filePath + "' not found");
        }
        if (!file.isFile()) {
            throw new ValidationException("Error: '" + filePath + "' is not a file");
        }
        if (!file.canRead()) {
            throw new ValidationException("Error: Cannot read file '" + filePath + "'");
        }
    }

    public static void validateOutputFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new ValidationException("Error: Output file path cannot be empty");
        }

        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && parentDir.exists() && !parentDir.canWrite()) {
            throw new ValidationException("Error: Cannot write to directory '" + parentDir.getPath() + "'");
        }

        if (file.exists() && !file.canWrite()) {
            throw new ValidationException("Error: Cannot overwrite file '" + filePath + "'");
        }
    }

    public static void validateData(List<DataRecord> records) {
        if (records == null) {
            throw new ValidationException("Error: Data cannot be null");
        }
    }

    public static void validateFileExtension(String filePath, String... supported) {
        String extension = getFileExtension(filePath);
        for (String ext : supported) {
            if (ext.equalsIgnoreCase(extension)) {
                return;
            }
        }
        throw new ValidationException("Error: Unsupported file format '." + extension + "'");
    }

    private static String getFileExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new ValidationException("Error: File path cannot be empty");
        }

        int lastDot = filePath.lastIndexOf('.');
        if (lastDot == -1 || lastDot == filePath.length() - 1) {
            throw new ValidationException("Error: File must have an extension");
        }

        return filePath.substring(lastDot + 1);
    }
}
