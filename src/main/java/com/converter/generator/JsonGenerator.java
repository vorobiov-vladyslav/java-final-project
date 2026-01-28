package com.converter.generator;

import com.converter.exception.ConversionException;
import com.converter.model.DataRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generator implementation for JSON files.
 */
public class JsonGenerator implements Generator {

    /**
     * Jackson ObjectMapper for JSON processing.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new JSON generator with indented output.
     */
    public JsonGenerator() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void generate(final List<DataRecord> data, final String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new ConversionException(
                        "Error: Cannot create directory '"
                                + parentDir.getPath() + "'");
            }
        }

        try {
            List<Map<String, String>> jsonData = data.stream()
                    .map(DataRecord::getFields)
                    .collect(Collectors.toList());

            objectMapper.writeValue(file, jsonData);
        } catch (IOException e) {
            throw new ConversionException(
                    "Error: Failed to write JSON file '"
                            + filePath + "': " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSupportedExtension() {
        return "json";
    }
}
