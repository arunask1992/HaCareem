package com.careem.domain.type.hibernate;

import com.careem.domain.ModeOfTransport;
import com.careem.domain.Position;
import com.fasterxml.jackson.core.type.TypeReference;

public class ModeOfTransportType extends BaseJsonType<ModeOfTransport> {

    public ModeOfTransportType() {
        super(new TypeReference<ModeOfTransport>() { });
    }

    @Override
    public Class<ModeOfTransport> returnedClass() {
        return ModeOfTransport.class;
    }
}
