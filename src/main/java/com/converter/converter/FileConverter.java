package com.converter.converter;

import com.converter.generator.Generator;
import com.converter.generator.GeneratorFactory;
import com.converter.model.DataRecord;
import com.converter.parser.Parser;
import com.converter.parser.ParserFactory;
import com.converter.validator.DataValidator;

import java.util.List;

public class FileConverter implements ConversionStrategy {

    private final Parser parser;
    private final Generator generator;

    public FileConverter(String inputPath, String outputPath) {
        this.parser = ParserFactory.createParser(inputPath);
        this.generator = GeneratorFactory.createGenerator(outputPath);
    }

    public FileConverter(Parser parser, Generator generator) {
        this.parser = parser;
        this.generator = generator;
    }

    @Override
    public void convert(String inputPath, String outputPath) {
        DataValidator.validateInputFile(inputPath);
        DataValidator.validateOutputFile(outputPath);

        List<DataRecord> records = parse(inputPath);

        DataValidator.validateData(records);

        generate(records, outputPath);
    }

    @Override
    public List<DataRecord> parse(String inputPath) {
        return parser.parse(inputPath);
    }

    @Override
    public void generate(List<DataRecord> records, String outputPath) {
        generator.generate(records, outputPath);
    }

    public Parser getParser() {
        return parser;
    }

    public Generator getGenerator() {
        return generator;
    }
}
