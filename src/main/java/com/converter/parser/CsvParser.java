package com.converter.parser;

import com.converter.exception.ParseException;
import com.converter.model.DataRecord;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser implements Parser {

    @Override
    public List<DataRecord> parse(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new ParseException("Error: Input file '" + filePath + "' not found");
        }
        if (!file.canRead()) {
            throw new ParseException("Error: Cannot read file '" + filePath + "'");
        }

        List<DataRecord> records = new ArrayList<>();

        try {
            char delimiter = detectDelimiter(filePath);
            CSVParser csvParser = new CSVParserBuilder()
                    .withSeparator(delimiter)
                    .build();

            try (CSVReader reader = new CSVReaderBuilder(new FileReader(file))
                    .withCSVParser(csvParser)
                    .build()) {
                List<String[]> allRows = reader.readAll();

                if (allRows.isEmpty()) {
                    return records;
                }

                String[] headers = allRows.get(0);
                for (int k = 0; k < headers.length; k++) {
                    headers[k] = headers[k].trim();
                }

                for (int i = 1; i < allRows.size(); i++) {
                    String[] row = allRows.get(i);

                    if (isEmptyRow(row)) {
                        continue;
                    }

                    DataRecord record = new DataRecord();

                    for (int j = 0; j < headers.length; j++) {
                        String value = (j < row.length) ? row[j].trim() : "";
                        record.setField(headers[j], value);
                    }

                    records.add(record);
                }
            }

        } catch (CsvException e) {
            throw new ParseException("Error: Invalid CSV syntax at line " + e.getLineNumber() + ": " + e.getMessage(), e);
        } catch (IOException e) {
            throw new ParseException("Error: Failed to read CSV file '" + filePath + "': " + e.getMessage(), e);
        }

        return records;
    }

    private char detectDelimiter(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String firstLine = br.readLine();
            if (firstLine == null) {
                return ',';
            }

            int commas = countChar(firstLine, ',');
            int semicolons = countChar(firstLine, ';');

            return semicolons > commas ? ';' : ',';
        }
    }

    private int countChar(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    private boolean isEmptyRow(String[] row) {
        for (String value : row) {
            if (value != null && !value.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getSupportedExtension() {
        return "csv";
    }
}
