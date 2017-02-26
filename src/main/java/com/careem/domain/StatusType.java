package com.careem.domain;

import com.careem.domain.jackson.PersistentEnumSerializer;
import com.careem.domain.type.hibernate.PersistentEnum;
import com.careem.exception.RecordNotFoundException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.NoArgsConstructor;

import static java.util.Arrays.asList;

@JsonSerialize(using = PersistentEnumSerializer.class)
@NoArgsConstructor
public enum StatusType implements PersistentEnum {
    ON_TRACK("on_track"),
    DELIVERED("delivered");
    private String type;

    StatusType(String type) {
        this.type = type;
    }

    @JsonCreator
    public static StatusType fromValue(String value) {
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
