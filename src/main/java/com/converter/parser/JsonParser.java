package com.converter.parser;

import com.converter.exception.ParseException;
import com.converter.exception.ValidationException;
import com.converter.model.DataRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Parser implementation for JSON files.
 */
public class JsonParser implements Parser {

    /**
     * Jackson ObjectMapper for JSON processing.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new JSON parser.
     */
    public JsonParser() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DataRecord> parse(final String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new ParseException(
                    "Error: Input file '" + filePath + "' not found");
        }
        if (!file.canRead()) {
            throw new ParseException(
                    "Error: Cannot read file '" + filePath + "'");
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            return parseJsonNode(rootNode);
        } catch (JsonProcessingException e) {
            String message = e.getOriginalMessage();
            long line = e.getLocation() != null
                    ? e.getLocation().getLineNr() : -1;
            if (line > 0) {
                throw new ParseException(
                        "Error: Invalid JSON syntax at line "
                                + line + ": " + message, e);
            }
            throw new ParseException(
                    "Error: Invalid JSON syntax: " + message, e);
        } catch (IOException e) {
            throw new ParseException(
                    "Error: Failed to read JSON file '"
                            + filePath + "': " + e.getMessage(), e);
        }
    }

    /**
     * Parses a JSON node into a list of data records.
     *
     * @param rootNode the root JSON node
     * @return a list of data records
     * @throws ValidationException if the JSON structure is invalid
     */
    private List<DataRecord> parseJsonNode(final JsonNode rootNode) {
        List<DataRecord> records = new ArrayList<>();

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                if (node.isObject()) {
                    records.add(parseObject(node));
                } else {
                    throw new ValidationException(
                            "Error: Array elements must be objects");
                }
            }
        } else if (rootNode.isObject()) {
            records.add(parseObject(rootNode));
        } else {
            throw new ValidationException(
                    "Error: JSON must be an object or array of objects");
        }

        return records;
    }

    /**
     * Parses a JSON object node into a data record.
     *
     * @param node the JSON object node
     * @return a data record containing the object's fields
     * @throws ValidationException if nested structures are found
     */
    private DataRecord parseObject(final JsonNode node) {
        DataRecord record = new DataRecord();
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode value = field.getValue();

            if (value.isObject() || value.isArray()) {
                throw new ValidationException(
                        "Error: Nested structures are not supported. "
                                + "Please use flat JSON");
            }

            String stringValue = value.isNull() ? "" : value.asText();
            record.setField(key, stringValue);
        }

        return record;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSupportedExtension() {
        return "json";
    }
}
