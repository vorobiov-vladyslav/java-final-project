package com.converter.converter;

import com.converter.exception.ConversionException;
import com.converter.exception.ValidationException;
import com.converter.model.DataRecord;
import com.converter.parser.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileConverterTest {

    @TempDir
    Path tempDir;

    @Test
    void testConvertJsonToCsv() throws IOException {
        String json = """
                [
                  {"id": "1", "name": "John"},
                  {"id": "2", "name": "Jane"}
                ]
                """;
        Path inputFile = tempDir.resolve("input.json");
        Path outputFile = tempDir.resolve("output.csv");
        Files.writeString(inputFile, json);

        FileConverter converter = new FileConverter(inputFile.toString(), outputFile.toString());
        converter.convert(inputFile.toString(), outputFile.toString());

        assertTrue(Files.exists(outputFile));
        List<String> lines = Files.readAllLines(outputFile);
        assertTrue(lines.size() >= 3);
        assertTrue(lines.get(0).contains("id"));
        assertTrue(lines.get(0).contains("name"));
    }

    @Test
    void testConvertJsonToXml() throws IOException {
        String json = """
                [{"id": "1", "name": "John"}]
                """;
        Path inputFile = tempDir.resolve("input.json");
        Path outputFile = tempDir.resolve("output.xml");
        Files.writeString(inputFile, json);

        FileConverter converter = new FileConverter(inputFile.toString(), outputFile.toString());
        converter.convert(inputFile.toString(), outputFile.toString());

        assertTrue(Files.exists(outputFile));
        String content = Files.readString(outputFile);
        assertTrue(content.contains("<records>"));
        assertTrue(content.contains("<id>1</id>"));
    }

    @Test
    void testConvertCsvToJson() throws IOException {
        String csv = """
                id,name
                1,John
                2,Jane
                """;
        Path inputFile = tempDir.resolve("input.csv");
        Path outputFile = tempDir.resolve("output.json");
        Files.writeString(inputFile, csv);

        FileConverter converter = new FileConverter(inputFile.toString(), outputFile.toString());
        converter.convert(inputFile.toString(), outputFile.toString());

        assertTrue(Files.exists(outputFile));
        String content = Files.readString(outputFile);
        assertTrue(content.contains("\"id\""));
        assertTrue(content.contains("\"name\""));
    }

    @Test
    void testConvertXmlToJson() throws IOException {
        String xml = """
                <?xml version="1.0"?>
                <records>
                  <record>
                    <id>1</id>
                    <name>John</name>
                  </record>
                </records>
                """;
        Path inputFile = tempDir.resolve("input.xml");
        Path outputFile = tempDir.resolve("output.json");
        Files.writeString(inputFile, xml);

        FileConverter converter = new FileConverter(inputFile.toString(), outputFile.toString());
        converter.convert(inputFile.toString(), outputFile.toString());

        assertTrue(Files.exists(outputFile));
        String content = Files.readString(outputFile);
        assertTrue(content.contains("\"id\""));
        assertTrue(content.contains("\"1\"") || content.contains(": \"1\""));
    }

    @Test
    void testConvertNonExistentFileThrowsException() {
        Path inputFile = tempDir.resolve("nonexistent.json");
        Path outputFile = tempDir.resolve("output.csv");

        FileConverter converter = new FileConverter(inputFile.toString(), outputFile.toString());

        assertThrows(ValidationException.class,
                () -> converter.convert(inputFile.toString(), outputFile.toString()));
    }

    @Test
    void testConvertUnsupportedFormatThrowsException() {
        assertThrows(ConversionException.class,
                () -> new FileConverter("input.txt", "output.csv"));
    }

    @Test
    void testParseReturnsDataRecords() throws IOException {
        String json = """
                [{"id": "1", "name": "John"}]
                """;
        Path inputFile = tempDir.resolve("input.json");
        Files.writeString(inputFile, json);

        FileConverter converter = new FileConverter(inputFile.toString(), tempDir.resolve("out.csv").toString());
        List<DataRecord> records = converter.parse(inputFile.toString());

        assertEquals(1, records.size());
        assertEquals("1", records.get(0).getField("id"));
    }

    @Test
    void testGetParser() throws IOException {
        Path inputFile = tempDir.resolve("test.json");
        Files.writeString(inputFile, "[]");

        FileConverter converter = new FileConverter(inputFile.toString(), tempDir.resolve("out.csv").toString());

        assertNotNull(converter.getParser());
        assertTrue(converter.getParser() instanceof JsonParser);
    }
}
