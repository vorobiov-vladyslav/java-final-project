package com.converter.parser;

import com.converter.exception.ParseException;
import com.converter.exception.ValidationException;
import com.converter.model.DataRecord;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser implementation for XML files.
 */
public class XmlParser implements Parser {

    /**
     * XML depth level for record elements.
     */
    private static final int RECORD_DEPTH = 2;

    /**
     * XML depth level for field elements.
     */
    private static final int FIELD_DEPTH = 3;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DataRecord> parse(final String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new ParseException(
                    "Error: Input file '" + filePath + "' not found");
        }
        if (!file.canRead()) {
            throw new ParseException(
                    "Error: Cannot read file '" + filePath + "'");
        }

        List<DataRecord> records = new ArrayList<>();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(
                XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        try (FileInputStream fis = new FileInputStream(file)) {
            XMLStreamReader reader = factory.createXMLStreamReader(fis);

            DataRecord currentRecord = null;
            String currentField = null;
            StringBuilder currentValue = new StringBuilder();
            int depth = 0;

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        depth++;
                        String elementName = reader.getLocalName();

                        if (depth == RECORD_DEPTH) {
                            currentRecord = new DataRecord();
                        } else if (depth == FIELD_DEPTH) {
                            currentField = elementName;
                            currentValue = new StringBuilder();
                        } else if (depth > FIELD_DEPTH) {
                            throw new ValidationException(
                                    "Error: Nested structures are not "
                                            + "supported. Please use flat XML");
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        if (currentField != null && !reader.isWhiteSpace()) {
                            currentValue.append(reader.getText());
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        if (depth == FIELD_DEPTH
                                && currentField != null
                                && currentRecord != null) {
                            currentRecord.setField(
                                    currentField,
                                    currentValue.toString().trim());
                            currentField = null;
                        } else if (depth == RECORD_DEPTH
                                && currentRecord != null) {
                            records.add(currentRecord);
                            currentRecord = null;
                        }
                        depth--;
                        break;

                    default:
                        break;
                }
            }

            reader.close();
        } catch (XMLStreamException e) {
            throw new ParseException(
                    "Error: Invalid XML syntax: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new ParseException(
                    "Error: Failed to read XML file '"
                            + filePath + "': " + e.getMessage(), e);
        }

        return records;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSupportedExtension() {
        return "xml";
    }
}
