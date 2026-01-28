package com.converter.converter;

import com.converter.model.DataRecord;

import java.util.List;

/**
 * Strategy interface for file conversion operations.
 */
public interface ConversionStrategy {

    /**
     * Converts a file from one format to another.
     *
     * @param inputPath  the path to the input file
     * @param outputPath the path to the output file
     */
    void convert(String inputPath, String outputPath);

    /**
     * Parses the input file and returns the data records.
     *
     * @param inputPath the path to the input file
     * @return a list of parsed data records
     */
    List<DataRecord> parse(String inputPath);

    /**
     * Generates an output file from the given data records.
     *
     * @param records    the data records to write
     * @param outputPath the path to the output file
     */
    void generate(List<DataRecord> records, String outputPath);
}
