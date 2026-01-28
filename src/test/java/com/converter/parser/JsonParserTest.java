package com.converter.parser;

import com.converter.exception.ParseException;
import com.converter.exception.ValidationException;
import com.converter.model.DataRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {

    private JsonParser parser;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        parser = new JsonParser();
    }

    @Test
    void testGetSupportedExtension() {
        assertEquals("json", parser.getSupportedExtension());
    }

    @Test
    void testParseValidJsonArray() throws IOException {
        String json = """
                [
                  {"id": "1", "name": "John"},
                  {"id": "2", "name": "Jane"}
                ]
                """;
        Path file = tempDir.resolve("test.json");
        Files.writeString(file, json);

        List<DataRecord> records = parser.parse(file.toString());

        assertEquals(2, records.size());
        assertEquals("1", records.get(0).getField("id"));
        assertEquals("John", records.get(0).getField("name"));
        assertEquals("2", records.get(1).getField("id"));
        assertEquals("Jane", records.get(1).getField("name"));
    }

    @Test
    void testParseValidJsonObject() throws IOException {
        String json = """
                {"id": "1", "name": "John"}
                """;
        Path file = tempDir.resolve("test.json");
        Files.writeString(file, json);

        List<DataRecord> records = parser.parse(file.toString());

        assertEquals(1, records.size());
        assertEquals("1", records.get(0).getField("id"));
        assertEquals("John", records.get(0).getField("name"));
    }

    @Test
    void testParseNonExistentFile() {
        ParseException exception = assertThrows(ParseException.class,
                () -> parser.parse("/nonexistent/file.json"));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testParseInvalidJson() throws IOException {
        String json = """
                {"id": "1", "name": "John"
                """;
        Path file = tempDir.resolve("invalid.json");
        Files.writeString(file, json);

        ParseException exception = assertThrows(ParseException.class,
                () -> parser.parse(file.toString()));
        assertTrue(exception.getMessage().contains("Invalid JSON syntax"));
    }

    @Test
    void testParseNestedStructureThrowsValidationException() throws IOException {
        String json = """
                [{"id": "1", "address": {"city": "NYC"}}]
                """;
        Path file = tempDir.resolve("nested.json");
        Files.writeString(file, json);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> parser.parse(file.toString()));
        assertTrue(exception.getMessage().contains("Nested structures are not supported"));
    }

    @Test
    void testParseJsonWithNullValues() throws IOException {
        String json = """
                [{"id": "1", "name": null}]
                """;
        Path file = tempDir.resolve("null.json");
        Files.writeString(file, json);

        List<DataRecord> records = parser.parse(file.toString());

        assertEquals(1, records.size());
        assertEquals("1", records.get(0).getField("id"));
        assertEquals("", records.get(0).getField("name"));
    }

    @Test
    void testParseEmptyArray() throws IOException {
        String json = "[]";
        Path file = tempDir.resolve("empty.json");
        Files.writeString(file, json);

        List<DataRecord> records = parser.parse(file.toString());

        assertTrue(records.isEmpty());
    }
}
