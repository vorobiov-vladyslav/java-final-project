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

public class JsonParser implements Parser {

    private final ObjectMapper objectMapper;

    public JsonParser() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<DataRecord> parse(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new ParseException("Error: Input file '" + filePath + "' not found");
        }
        if (!file.canRead()) {
            throw new ParseException("Error: Cannot read file '" + filePath + "'");
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            return parseJsonNode(rootNode);
        } catch (JsonProcessingException e) {
            String message = e.getOriginalMessage();
            long line = e.getLocation() != null ? e.getLocation().getLineNr() : -1;
            if (line > 0) {
                throw new ParseException("Error: Invalid JSON syntax at line " + line + ": " + message, e);
            }
            throw new ParseException("Error: Invalid JSON syntax: " + message, e);
        } catch (IOException e) {
            throw new ParseException("Error: Failed to read JSON file '" + filePath + "': " + e.getMessage(), e);
        }
    }

    private List<DataRecord> parseJsonNode(JsonNode rootNode) {
        List<DataRecord> records = new ArrayList<>();

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                if (node.isObject()) {
                    records.add(parseObject(node));
                } else {
                    throw new ValidationException("Error: Array elements must be objects");
                }
            }
        } else if (rootNode.isObject()) {
            records.add(parseObject(rootNode));
        } else {
            throw new ValidationException("Error: JSON must be an object or array of objects");
        }

        return records;
    }

    private DataRecord parseObject(JsonNode node) {
        DataRecord record = new DataRecord();
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode value = field.getValue();

            if (value.isObject() || value.isArray()) {
                throw new ValidationException("Error: Nested structures are not supported. Please use flat JSON");
            }

            String stringValue = value.isNull() ? "" : value.asText();
            record.setField(key, stringValue);
        }

        return record;
    }

    @Override
    public String getSupportedExtension() {
        return "json";
    }
}
