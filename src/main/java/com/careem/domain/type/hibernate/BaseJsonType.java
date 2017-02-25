package com.careem.domain.type.hibernate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
public abstract class BaseJsonType<T> extends BaseVarCharType<T> {

    protected ObjectMapper objectMapper;
    private final TypeReference<T> valueTypeRef;

    public BaseJsonType(TypeReference<T> valueTypeRef) {
        this.valueTypeRef = valueTypeRef;
        objectMapper = new ObjectMapper();
    }

    @Override
    protected T get(String jsonString) throws IOException {
        return objectMapper.readValue(jsonString, valueTypeRef);
    }

    @Override
    protected String set(T value) throws IOException {
        return objectMapper.writeValueAsString(value);
    }

}
