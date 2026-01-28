package com.converter.generator;

import com.converter.exception.ConversionException;
import com.converter.model.DataRecord;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class XmlGenerator implements Generator {

    private static final String ROOT_ELEMENT = "records";
    private static final String RECORD_ELEMENT = "record";

    @Override
    public void generate(List<DataRecord> data, String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new ConversionException("Error: Cannot create directory '" + parentDir.getPath() + "'");
            }
        }

        XMLOutputFactory factory = XMLOutputFactory.newInstance();

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

            XMLStreamWriter writer = factory.createXMLStreamWriter(osw);

            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeCharacters("\n");
            writer.writeStartElement(ROOT_ELEMENT);
            writer.writeCharacters("\n");

            for (DataRecord record : data) {
                writeRecord(writer, record);
            }

            writer.writeEndElement();
            writer.writeCharacters("\n");
            writer.writeEndDocument();
            writer.flush();
            writer.close();

        } catch (XMLStreamException e) {
            throw new ConversionException("Error: Failed to generate XML: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new ConversionException("Error: Failed to write XML file '" + filePath + "': " + e.getMessage(), e);
        }
    }

    private void writeRecord(XMLStreamWriter writer, DataRecord record) throws XMLStreamException {
        writer.writeCharacters("  ");
        writer.writeStartElement(RECORD_ELEMENT);
        writer.writeCharacters("\n");

        for (Map.Entry<String, String> field : record.getFields().entrySet()) {
            writer.writeCharacters("    ");
            writer.writeStartElement(sanitizeElementName(field.getKey()));
            writer.writeCharacters(field.getValue() != null ? field.getValue() : "");
            writer.writeEndElement();
            writer.writeCharacters("\n");
        }

        writer.writeCharacters("  ");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    private String sanitizeElementName(String name) {
        if (name == null || name.isEmpty()) {
            return "field";
        }

        StringBuilder sb = new StringBuilder();
        char first = name.charAt(0);

        if (Character.isLetter(first) || first == '_') {
            sb.append(first);
        } else {
            sb.append('_');
            if (Character.isDigit(first)) {
                sb.append(first);
            }
        }

        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == '.') {
                sb.append(c);
            } else {
                sb.append('_');
            }
        }

        return sb.toString();
    }

    @Override
    public String getSupportedExtension() {
        return "xml";
    }
}
