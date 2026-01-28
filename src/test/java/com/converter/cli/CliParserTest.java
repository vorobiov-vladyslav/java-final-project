package com.converter.cli;

import com.converter.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CliParserTest {

    private CliParser parser;

    @BeforeEach
    void setUp() {
        parser = new CliParser();
    }

    @Test
    void testParseValidArguments() {
        String[] args = {"--input", "input.json", "--output", "output.csv"};

        parser.parse(args);

        assertEquals("input.json", parser.getInputPath());
        assertEquals("output.csv", parser.getOutputPath());
        assertFalse(parser.isHelpRequested());
    }

    @Test
    void testParseWithReversedArguments() {
        String[] args = {"--output", "output.csv", "--input", "input.json"};

        parser.parse(args);

        assertEquals("input.json", parser.getInputPath());
        assertEquals("output.csv", parser.getOutputPath());
    }

    @Test
    void testParseMissingInputThrowsException() {
        String[] args = {"--output", "output.csv"};

        ValidationException exception = assertThrows(ValidationException.class,
                () -> parser.parse(args));
        assertTrue(exception.getMessage().contains("--input"));
    }

    @Test
    void testParseMissingOutputThrowsException() {
        String[] args = {"--input", "input.json"};

        ValidationException exception = assertThrows(ValidationException.class,
                () -> parser.parse(args));
        assertTrue(exception.getMessage().contains("--output"));
    }

    @Test
    void testParseEmptyArgumentsThrowsException() {
        String[] args = {};

        assertThrows(ValidationException.class, () -> parser.parse(args));
    }

    @Test
    void testParseHelpFlag() {
        String[] args = {"--help"};

        parser.parse(args);

        assertTrue(parser.isHelpRequested());
    }

    @Test
    void testParseShortHelpFlag() {
        String[] args = {"-h"};

        parser.parse(args);

        assertTrue(parser.isHelpRequested());
    }

    @Test
    void testParseHelpWithOtherArguments() {
        String[] args = {"--help", "--input", "input.json", "--output", "output.csv"};

        parser.parse(args);

        assertTrue(parser.isHelpRequested());
    }

    @Test
    void testGetHelpMessageNotEmpty() {
        String helpMessage = CliParser.getHelpMessage();

        assertNotNull(helpMessage);
        assertFalse(helpMessage.isEmpty());
        assertTrue(helpMessage.contains("--input"));
        assertTrue(helpMessage.contains("--output"));
        assertTrue(helpMessage.contains("JSON"));
        assertTrue(helpMessage.contains("XML"));
        assertTrue(helpMessage.contains("CSV"));
    }

    @Test
    void testParseWithPathsContainingSpaces() {
        String[] args = {"--input", "path with spaces/input.json", "--output", "another path/output.csv"};

        parser.parse(args);

        assertEquals("path with spaces/input.json", parser.getInputPath());
        assertEquals("another path/output.csv", parser.getOutputPath());
    }
}
