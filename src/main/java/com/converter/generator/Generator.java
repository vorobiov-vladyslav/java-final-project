package com.converter.generator;

import com.converter.model.DataRecord;

import java.util.List;

public interface Generator {

    void generate(List<DataRecord> data, String filePath);

    String getSupportedExtension();
}
