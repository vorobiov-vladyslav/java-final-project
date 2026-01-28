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

class XmlGeneratorTest {

    private XmlGenerator generator;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        generator = new XmlGenerator();
    }

    @Test
    void testGetSupportedExtension() {
        assertEquals("xml", generator.getSupportedExtension());
    }

    @Test
    void testGenerateValidXml() throws IOException {
        List<DataRecord> records = new ArrayList<>();
        records.add(new DataRecord(Map.of("id", "1", "name", "John")));
        records.add(new DataRecord(Map.of("id", "2", "name", "Jane")));

        Path file = tempDir.resolve("output.xml");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        String content = Files.readString(file);
        assertTrue(content.contains("<?xml"));
        assertTrue(content.contains("<records>"));
        assertTrue(content.contains("<record>"));
        assertTrue(content.contains("<id>1</id>") || content.contains("<id>2</id>"));
        assertTrue(content.contains("<name>John</name>") || content.contains("<name>Jane</name>"));
        assertTrue(content.contains("</records>"));
    }

    @Test
    void testGenerateEmptyList() throws IOException {
        List<DataRecord> records = new ArrayList<>();

        Path file = tempDir.resolve("empty.xml");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        String content = Files.readString(file);
        assertTrue(content.contains("<records>"));
        assertTrue(content.contains("</records>"));
    }

    @Test
    void testGenerateCreatesParentDirectories() throws IOException {
        List<DataRecord> records = new ArrayList<>();
        records.add(new DataRecord(Map.of("id", "1")));

        Path file = tempDir.resolve("subdir/output.xml");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        assertTrue(Files.exists(file.getParent()));
    }

    @Test
    void testGenerateWithInvalidElementName() throws IOException {
        List<DataRecord> records = new ArrayList<>();
        records.add(new DataRecord(Map.of("123invalid", "value", "valid_name", "value2")));

        Path file = tempDir.resolve("invalid-names.xml");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        String content = Files.readString(file);
        assertTrue(content.contains("<_123invalid>"));
        assertTrue(content.contains("<valid_name>"));
    }

    @Test
    void testGenerateWithEmptyValues() throws IOException {
        List<DataRecord> records = new ArrayList<>();
        records.add(new DataRecord(Map.of("id", "1", "name", "")));

        Path file = tempDir.resolve("empty-values.xml");
        generator.generate(records, file.toString());

        assertTrue(Files.exists(file));
        String content = Files.readString(file);
        assertTrue(content.contains("<name></name>") || content.contains("<name/>"));
    }
}
