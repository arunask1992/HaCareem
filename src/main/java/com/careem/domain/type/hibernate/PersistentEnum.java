package com.careem.domain.type.hibernate;

import com.careem.domain.jackson.PersistentEnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize(using = PersistentEnumSerializer.class)
public interface PersistentEnum {
    String toString();

    class Helper {
        public static <T> T findEnum(String value, List<T> values) {
            return values.stream().filter(entry -> entry.toString().equalsIgnoreCase(value)).findFirst().get();
        }
    }
}
