package com.converter.generator;

import com.converter.model.DataRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonGeneratorTest {

    private JsonGenerator generator;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        generator = new JsonGenerator();
    }

    @Test
    void testGetSupportedExtension() {
        assertEquals("json", generator.getSupportedExtension());
    }

    @Test
    void testGenerateValidJson() throws IOException {
        List<DataRecord> records = new ArrayList<>();
        records.add(new DataRecord(Map.of("id", "1", "name", "John")));
        records.add(new DataRecord(Map.of("id", "2", "name", "Jane")));

        Path file = tempDir.resolve("output.json");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        String content = Files.readString(file);
        assertTrue(content.contains("\"id\""));
        assertTrue(content.contains("\"name\""));
        assertTrue(content.contains("John"));
        assertTrue(content.contains("Jane"));
    }

    @Test
    void testGenerateEmptyList() throws IOException {
        List<DataRecord> records = new ArrayList<>();

        Path file = tempDir.resolve("empty.json");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        String content = Files.readString(file).trim();
        assertEquals("[ ]", content);
    }

    @Test
    void testGenerateCreatesParentDirectories() throws IOException {
        List<DataRecord> records = new ArrayList<>();
        records.add(new DataRecord(Map.of("id", "1")));

        Path file = tempDir.resolve("subdir/output.json");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        assertTrue(Files.exists(file.getParent()));
    }

    @Test
    void testGenerateWithSpecialCharacters() throws IOException {
        List<DataRecord> records = new ArrayList<>();
        records.add(new DataRecord(Map.of("name", "John \"The Great\"", "description", "Line1\nLine2")));

        Path file = tempDir.resolve("special.json");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        String content = Files.readString(file);
        assertTrue(content.contains("John \\\"The Great\\\""));
    }
}
