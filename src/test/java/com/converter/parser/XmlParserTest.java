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

class XmlParserTest {

    private XmlParser parser;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        parser = new XmlParser();
    }

    @Test
    void testGetSupportedExtension() {
        assertEquals("xml", parser.getSupportedExtension());
    }

    @Test
    void testParseValidXml() throws IOException {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <records>
                  <record>
                    <id>1</id>
                    <name>John</name>
                  </record>
                  <record>
                    <id>2</id>
                    <name>Jane</name>
                  </record>
                </records>
                """;
        Path file = tempDir.resolve("test.xml");
        Files.writeString(file, xml);

        List<DataRecord> records = parser.parse(file.toString());

        assertEquals(2, records.size());
        assertEquals("1", records.get(0).getField("id"));
        assertEquals("John", records.get(0).getField("name"));
        assertEquals("2", records.get(1).getField("id"));
        assertEquals("Jane", records.get(1).getField("name"));
    }

    @Test
    void testParseNonExistentFile() {
        ParseException exception = assertThrows(ParseException.class,
                () -> parser.parse("/nonexistent/file.xml"));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testParseInvalidXml() throws IOException {
        String xml = """
                <?xml version="1.0"?>
                <records>
                  <record>
                    <id>1</id>
                """;
        Path file = tempDir.resolve("invalid.xml");
        Files.writeString(file, xml);

        ParseException exception = assertThrows(ParseException.class,
                () -> parser.parse(file.toString()));
        assertTrue(exception.getMessage().contains("Invalid XML syntax"));
    }

    @Test
    void testParseNestedXmlThrowsValidationException() throws IOException {
        String xml = """
                <?xml version="1.0"?>
                <records>
                  <record>
                    <id>1</id>
                    <address>
                      <city>NYC</city>
                    </address>
                  </record>
                </records>
                """;
        Path file = tempDir.resolve("nested.xml");
        Files.writeString(file, xml);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> parser.parse(file.toString()));
        assertTrue(exception.getMessage().contains("Nested structures are not supported"));
    }

    @Test
    void testParseEmptyRecords() throws IOException {
        String xml = """
                <?xml version="1.0"?>
                <records>
                </records>
                """;
        Path file = tempDir.resolve("empty.xml");
        Files.writeString(file, xml);

        List<DataRecord> records = parser.parse(file.toString());

        assertTrue(records.isEmpty());
    }

    @Test
    void testParseXmlWithEmptyValues() throws IOException {
        String xml = """
                <?xml version="1.0"?>
                <records>
                  <record>
                    <id>1</id>
                    <name></name>
                  </record>
                </records>
                """;
        Path file = tempDir.resolve("empty-values.xml");
        Files.writeString(file, xml);

        List<DataRecord> records = parser.parse(file.toString());

        assertEquals(1, records.size());
        assertEquals("1", records.get(0).getField("id"));
        assertEquals("", records.get(0).getField("name"));
    }
}
