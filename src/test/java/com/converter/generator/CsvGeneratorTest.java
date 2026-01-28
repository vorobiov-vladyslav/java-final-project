package com.converter.generator;

import com.converter.model.DataRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CsvGeneratorTest {

    private CsvGenerator generator;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        generator = new CsvGenerator();
    }

    @Test
    void testGetSupportedExtension() {
        assertEquals("csv", generator.getSupportedExtension());
    }

    @Test
    void testGenerateValidCsv() throws IOException {
        List<DataRecord> records = new ArrayList<>();
        Map<String, String> fields1 = new LinkedHashMap<>();
        fields1.put("id", "1");
        fields1.put("name", "John");
        records.add(new DataRecord(fields1));

        Map<String, String> fields2 = new LinkedHashMap<>();
        fields2.put("id", "2");
        fields2.put("name", "Jane");
        records.add(new DataRecord(fields2));

        Path file = tempDir.resolve("output.csv");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        List<String> lines = Files.readAllLines(file);
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).contains("id"));
        assertTrue(lines.get(0).contains("name"));
        assertTrue(lines.get(1).contains("1"));
        assertTrue(lines.get(1).contains("John"));
    }

    @Test
    void testGenerateEmptyList() throws IOException {
        List<DataRecord> records = new ArrayList<>();

        Path file = tempDir.resolve("empty.csv");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        assertEquals(0, Files.size(file));
    }

    @Test
    void testGenerateCreatesParentDirectories() throws IOException {
        List<DataRecord> records = new ArrayList<>();
        records.add(new DataRecord(Map.of("id", "1")));

        Path file = tempDir.resolve("subdir/output.csv");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        assertTrue(Files.exists(file.getParent()));
    }

    @Test
    void testGenerateWithSpecialCharacters() throws IOException {
        List<DataRecord> records = new ArrayList<>();
        records.add(new DataRecord(Map.of("name", "John, Jr.", "description", "He said \"Hello\"")));

        Path file = tempDir.resolve("special.csv");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        String content = Files.readString(file);
        assertTrue(content.contains("\"John, Jr.\""));
    }

    @Test
    void testGenerateEmptyDataOverwritesExistingFile() throws IOException {
        Path file = tempDir.resolve("existing.csv");
        Files.writeString(file, "old,content\n1,2");

        generator.generate(List.of(), file.toString());

        assertEquals("", Files.readString(file));
    }

    @Test
    void testGenerateWithMixedFields() throws IOException {
        List<DataRecord> records = new ArrayList<>();
        Map<String, String> fields1 = new LinkedHashMap<>();
        fields1.put("id", "1");
        fields1.put("name", "John");
        records.add(new DataRecord(fields1));

        Map<String, String> fields2 = new LinkedHashMap<>();
        fields2.put("id", "2");
        fields2.put("name", "Jane");
        fields2.put("email", "jane@example.com");
        records.add(new DataRecord(fields2));

        Path file = tempDir.resolve("mixed.csv");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        List<String> lines = Files.readAllLines(file);
        assertTrue(lines.get(0).contains("email"));
        assertEquals(3, lines.get(1).split(",").length);
    }
}
