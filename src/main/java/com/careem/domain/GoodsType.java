package com.careem.domain;

import com.careem.domain.type.hibernate.PersistentEnum;
import com.careem.exception.RecordNotFoundException;
import com.fasterxml.jackson.annotation.JsonCreator;

import static java.util.Arrays.asList;

public enum GoodsType implements PersistentEnum {
    ELECTRICAL_APPLICANCE("electrical_appliance"),
    PERISHABLE_GOODS("perishable"),
    GENERAL("general"),
    SAFE_DELIVERY("safe"),
    LIQUID("liquids");
    private String type;

    GoodsType(String type) {
        this.type = type;
    }

    @JsonCreator
    public static GoodsType fromValue(String value) {
        return asList(values())
                .stream()
                .filter(tenderStatus -> tenderStatus.toString().equals(value))
                .findFirst().orElseThrow(() -> new RecordNotFoundException(String.format("Could not find type %s", value)));
    }

    @Override
    public String toString() {
        return type;
    }
}
