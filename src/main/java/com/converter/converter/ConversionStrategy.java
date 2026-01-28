package com.converter.converter;

import com.converter.model.DataRecord;

import java.util.List;

public interface ConversionStrategy {

    void convert(String inputPath, String outputPath);

    List<DataRecord> parse(String inputPath);

    void generate(List<DataRecord> records, String outputPath);
}
