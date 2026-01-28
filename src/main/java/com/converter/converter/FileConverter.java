package com.converter.converter;

import com.converter.generator.Generator;
import com.converter.generator.GeneratorFactory;
import com.converter.model.DataRecord;
import com.converter.parser.Parser;
import com.converter.parser.ParserFactory;
import com.converter.validator.DataValidator;

import java.util.List;

/**
 * Implementation of file conversion between different formats.
 */
public class FileConverter implements ConversionStrategy {

    /**
     * The parser used to read input files.
     */
    private final Parser parser;

    /**
     * The generator used to write output files.
     */
    private final Generator generator;

    /**
     * Constructs a file converter for the given input and output file paths.
     *
     * @param inputPath  the path to the input file
     * @param outputPath the path to the output file
     */
    public FileConverter(final String inputPath, final String outputPath) {
        this.parser = ParserFactory.createParser(inputPath);
        this.generator = GeneratorFactory.createGenerator(outputPath);
    }

    /**
     * Constructs a file converter with the given parser and generator.
     *
     * @param inputParser   the parser to use for reading input
     * @param outputGenerator the generator to use for writing output
     */
    public FileConverter(final Parser inputParser,
                         final Generator outputGenerator) {
        this.parser = inputParser;
        this.generator = outputGenerator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void convert(final String inputPath, final String outputPath) {
        DataValidator.validateInputFile(inputPath);
        DataValidator.validateOutputFile(outputPath);

        List<DataRecord> records = parse(inputPath);

        DataValidator.validateData(records);

        generate(records, outputPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DataRecord> parse(final String inputPath) {
        return parser.parse(inputPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void generate(final List<DataRecord> records,
                         final String outputPath) {
        generator.generate(records, outputPath);
    }

    /**
     * Returns the parser used by this converter.
     *
     * @return the parser instance
     */
    public Parser getParser() {
        return parser;
    }

    /**
     * Returns the generator used by this converter.
     *
     * @return the generator instance
     */
    public Generator getGenerator() {
        return generator;
    }
}
