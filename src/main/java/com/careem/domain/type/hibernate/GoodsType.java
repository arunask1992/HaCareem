package com.careem.domain.type.hibernate;

import com.careem.domain.Position;
import com.fasterxml.jackson.core.type.TypeReference;

public class GoodsType extends BaseJsonType<com.careem.domain.GoodsType> {

    public GoodsType() {
        super(new TypeReference<com.careem.domain.GoodsType>() { });
    }

    @Override
    public Class<com.careem.domain.GoodsType> returnedClass() {
        return com.careem.domain.GoodsType.class;
    }
}
