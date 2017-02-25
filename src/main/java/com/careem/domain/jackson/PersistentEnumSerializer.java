package com.careem.domain.jackson;

import com.careem.domain.type.hibernate.PersistentEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PersistentEnumSerializer extends JsonSerializer<PersistentEnum> {
    @Override
    public void serialize(PersistentEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(value.toString());
    }
}
