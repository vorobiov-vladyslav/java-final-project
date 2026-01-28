package com.converter.generator;

import com.converter.model.DataRecord;

import java.util.List;

/**
 * Interface for generating output files from data records.
 */
public interface Generator {

    /**
     * Generates an output file from the given data records.
     *
     * @param data     the list of data records to write
     * @param filePath the path to the output file
     */
    void generate(List<DataRecord> data, String filePath);

    /**
     * Returns the file extension supported by this generator.
     *
     * @return the supported file extension
     */
    String getSupportedExtension();
}
