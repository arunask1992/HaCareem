package com.careem.domain;

import com.careem.domain.jackson.PersistentEnumSerializer;
import com.careem.domain.type.hibernate.PersistentEnum;
import com.careem.exception.RecordNotFoundException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static java.util.Arrays.asList;

@JsonSerialize(using = PersistentEnumSerializer.class)

public enum GoodTransferType implements PersistentEnum {
    WITHIN_CITY("intra_city"),
    OUTSIDE_CITY("inter_city");
    private String type;

    GoodTransferType(String type) {
        this.type = type;
    }

    @JsonCreator
    public static GoodTransferType fromValue(String value) {
        return asList(values())
                .stream()
                .filter(tenderStatus -> tenderStatus.toString().equals(value))
                .findFirst().orElseThrow(() -> new RecordNotFoundException(String.format("Could not find type %s", value)));
    }

    @Override
    public String toString() {
        return type;
    }

    public boolean isWithinCity() {
        return this.equals(fromValue("intra_city"));
    }
}
