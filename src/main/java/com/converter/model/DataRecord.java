package com.converter.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class DataRecord {

    private final Map<String, String> fields;

    public DataRecord() {
        this.fields = new LinkedHashMap<>();
    }

    public DataRecord(Map<String, String> fields) {
        this.fields = new LinkedHashMap<>(fields);
    }

    public Map<String, String> getFields() {
        return new LinkedHashMap<>(fields);
    }

    public void setField(String key, String value) {
        fields.put(key, value);
    }

    public String getField(String key) {
        return fields.get(key);
    }

    public boolean hasField(String key) {
        return fields.containsKey(key);
    }

    public int size() {
        return fields.size();
    }

    public boolean isEmpty() {
        return fields.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataRecord that = (DataRecord) o;
        return Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fields);
    }

    @Override
    public String toString() {
        return "DataRecord{" + fields + "}";
    }
}
