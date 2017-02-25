package com.careem.domain.type.hibernate;

import com.careem.domain.Position;
import com.fasterxml.jackson.core.type.TypeReference;

public class PositionType extends BaseJsonType<Position> {

    public PositionType() {
        super(new TypeReference<Position>() { });
    }

    @Override
    public Class<Position> returnedClass() {
        return Position.class;
    }
}
