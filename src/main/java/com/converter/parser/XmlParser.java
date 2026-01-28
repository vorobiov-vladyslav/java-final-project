package com.converter.parser;

import com.converter.exception.ParseException;
import com.converter.exception.ValidationException;
import com.converter.model.DataRecord;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlParser implements Parser {

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
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        try (FileInputStream fis = new FileInputStream(file)) {
            XMLStreamReader reader = factory.createXMLStreamReader(fis);

            String rootElement = null;
            String recordElement = null;
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

                        if (depth == 1) {
                            rootElement = elementName;
                        } else if (depth == 2) {
                            recordElement = elementName;
                            currentRecord = new DataRecord();
                        } else if (depth == 3) {
                            currentField = elementName;
                            currentValue = new StringBuilder();
                        } else if (depth > 3) {
                            throw new ValidationException("Error: Nested structures are not supported. Please use flat XML");
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        if (currentField != null && !reader.isWhiteSpace()) {
                            currentValue.append(reader.getText());
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        if (depth == 3 && currentField != null && currentRecord != null) {
                            currentRecord.setField(currentField, currentValue.toString().trim());
                            currentField = null;
                        } else if (depth == 2 && currentRecord != null) {
                            records.add(currentRecord);
                            currentRecord = null;
                        }
                        depth--;
                        break;
                }
            }

            reader.close();
        } catch (XMLStreamException e) {
            throw new ParseException("Error: Invalid XML syntax: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new ParseException("Error: Failed to read XML file '" + filePath + "': " + e.getMessage(), e);
        }

        return records;
    }

    @Override
    public String getSupportedExtension() {
        return "xml";
    }
}
