package com.converter.parser;

import com.converter.model.DataRecord;

import java.util.List;

public interface Parser {

    List<DataRecord> parse(String filePath);

    String getSupportedExtension();
}
