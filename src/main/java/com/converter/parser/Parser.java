package com.converter.parser;

import com.converter.model.DataRecord;

import java.util.List;

/**
 * Interface for parsing input files into data records.
 */
public interface Parser {

    /**
     * Parses a file and returns a list of data records.
     *
     * @param filePath the path to the file to parse
     * @return a list of parsed data records
     */
    List<DataRecord> parse(String filePath);

    /**
     * Returns the file extension supported by this parser.
     *
     * @return the supported file extension
     */
    String getSupportedExtension();
}
