package com.converter.parser;

import com.converter.exception.ParseException;
import com.converter.model.DataRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvParserTest {

    private CsvParser parser;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        parser = new CsvParser();
    }

    @Test
    void testGetSupportedExtension() {
        assertEquals("csv", parser.getSupportedExtension());
    }

    @Test
    void testParseValidCsv() throws IOException {
        String csv = """
                id,name,email
                1,John,john@example.com
                2,Jane,jane@example.com
                """;
        Path file = tempDir.resolve("test.csv");
        Files.writeString(file, csv);

        List<DataRecord> records = parser.parse(file.toString());

        assertEquals(2, records.size());
        assertEquals("1", records.get(0).getField("id"));
        assertEquals("John", records.get(0).getField("name"));
        assertEquals("john@example.com", records.get(0).getField("email"));
        assertEquals("2", records.get(1).getField("id"));
        assertEquals("Jane", records.get(1).getField("name"));
    }

    @Test
    void testParseNonExistentFile() {
        ParseException exception = assertThrows(ParseException.class,
                () -> parser.parse("/nonexistent/file.csv"));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testParseEmptyCsv() throws IOException {
        String csv = "";
        Path file = tempDir.resolve("empty.csv");
        Files.writeString(file, csv);

        List<DataRecord> records = parser.parse(file.toString());

        assertTrue(records.isEmpty());
    }

    @Test
    void testParseCsvWithHeaderOnly() throws IOException {
        String csv = "id,name,email\n";
        Path file = tempDir.resolve("header-only.csv");
        Files.writeString(file, csv);

        List<DataRecord> records = parser.parse(file.toString());

        assertTrue(records.isEmpty());
    }

    @Test
    void testParseCsvWithQuotedValues() throws IOException {
        String csv = "id,name,description\n" +
                "1,\"John Doe\",\"Contains, comma\"\n" +
                "2,\"Jane\",\"Contains \"\"quotes\"\"\"\n";
        Path file = tempDir.resolve("quoted.csv");
        Files.writeString(file, csv);

        List<DataRecord> records = parser.parse(file.toString());

        assertEquals(2, records.size());
        assertEquals("John Doe", records.get(0).getField("name"));
        assertEquals("Contains, comma", records.get(0).getField("description"));
        assertEquals("Contains \"quotes\"", records.get(1).getField("description"));
    }

    @Test
    void testParseCsvWithMissingValues() throws IOException {
        String csv = """
                id,name,email
                1,John
                2,Jane,jane@example.com
                """;
        Path file = tempDir.resolve("missing.csv");
        Files.writeString(file, csv);

        List<DataRecord> records = parser.parse(file.toString());

        assertEquals(2, records.size());
        assertEquals("1", records.get(0).getField("id"));
        assertEquals("John", records.get(0).getField("name"));
        assertEquals("", records.get(0).getField("email"));
    }

    @Test
    void testParseSemicolonDelimitedCsv() throws IOException {
        String csv = """
                Username; Identifier;First name;Last name
                booker12;9012;Rachel;Booker
                grey07;2070;Laura;Grey
                johnson81;4081;Craig;Johnson
                """;
        Path file = tempDir.resolve("semicolon.csv");
        Files.writeString(file, csv);

        List<DataRecord> records = parser.parse(file.toString());

        assertEquals(3, records.size());
        assertEquals("booker12", records.get(0).getField("Username"));
        assertEquals("9012", records.get(0).getField("Identifier"));
        assertEquals("Rachel", records.get(0).getField("First name"));
        assertEquals("Booker", records.get(0).getField("Last name"));
        assertEquals("grey07", records.get(1).getField("Username"));
        assertEquals("Laura", records.get(1).getField("First name"));
        assertEquals("johnson81", records.get(2).getField("Username"));
        assertEquals("Craig", records.get(2).getField("First name"));
    }

    @Test
    void testParseCsvWithTrailingNewline() throws IOException {
        String csv = "Username,Identifier,First name,Last name\n" +
                "booker12,9012,Rachel,Booker\n" +
                "grey07,2070,Laura,Grey\n" +
                "\n";
        Path file = tempDir.resolve("trailing-newline.csv");
        Files.writeString(file, csv);

        List<DataRecord> records = parser.parse(file.toString());

        assertEquals(2, records.size());
        assertEquals("booker12", records.get(0).getField("Username"));
        assertEquals("grey07", records.get(1).getField("Username"));
    }
}
