package com.converter.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a single data record with key-value field pairs.
 */
public class DataRecord {

    /**
     * The fields of this record stored as key-value pairs.
     */
    private final Map<String, String> fields;

    /**
     * Constructs an empty data record.
     */
    public DataRecord() {
        this.fields = new LinkedHashMap<>();
    }

    /**
     * Constructs a data record with the given fields.
     *
     * @param initialFields the initial fields for this record
     */
    public DataRecord(final Map<String, String> initialFields) {
        this.fields = new LinkedHashMap<>(initialFields);
    }

    /**
     * Returns a copy of all fields in this record.
     *
     * @return a map containing all field key-value pairs
     */
    public Map<String, String> getFields() {
        return new LinkedHashMap<>(fields);
    }

    /**
     * Sets a field value in this record.
     *
     * @param key   the field name
     * @param value the field value
     */
    public void setField(final String key, final String value) {
        fields.put(key, value);
    }

    /**
     * Gets the value of a field by its key.
     *
     * @param key the field name
     * @return the field value, or null if not present
     */
    public String getField(final String key) {
        return fields.get(key);
    }

    /**
     * Checks if this record contains a field with the given key.
     *
     * @param key the field name to check
     * @return true if the field exists, false otherwise
     */
    public boolean hasField(final String key) {
        return fields.containsKey(key);
    }

    /**
     * Returns the number of fields in this record.
     *
     * @return the field count
     */
    public int size() {
        return fields.size();
    }

    /**
     * Checks if this record has no fields.
     *
     * @return true if the record is empty, false otherwise
     */
    public boolean isEmpty() {
        return fields.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataRecord that = (DataRecord) o;
        return Objects.equals(fields, that.fields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(fields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DataRecord{" + fields + "}";
    }
}
