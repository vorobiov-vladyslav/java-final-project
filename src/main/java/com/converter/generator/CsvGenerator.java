package com.converter.generator;

import com.converter.exception.ConversionException;
import com.converter.model.DataRecord;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CsvGenerator implements Generator {

    @Override
    public void generate(List<DataRecord> data, String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new ConversionException("Error: Cannot create directory '" + parentDir.getPath() + "'");
            }
        }

        if (data.isEmpty()) {
            try {
                new FileWriter(file).close();
            } catch (IOException e) {
                throw new ConversionException("Error: Failed to create CSV file '" + filePath + "': " + e.getMessage(), e);
            }
            return;
        }

        Set<String> headers = new LinkedHashSet<>();
        for (DataRecord record : data) {
            headers.addAll(record.getFields().keySet());
        }

        String[] headerArray = headers.toArray(new String[0]);

        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            writer.writeNext(headerArray);

            for (DataRecord record : data) {
                String[] row = new String[headerArray.length];
                for (int i = 0; i < headerArray.length; i++) {
                    String value = record.getField(headerArray[i]);
                    row[i] = value != null ? value : "";
                }
                writer.writeNext(row);
            }

        } catch (IOException e) {
            throw new ConversionException("Error: Failed to write CSV file '" + filePath + "': " + e.getMessage(), e);
        }
    }

    @Override
    public String getSupportedExtension() {
        return "csv";
    }
}
