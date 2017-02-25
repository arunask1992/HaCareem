package com.careem.domain;

public class Car extends Resource {
    public static final String TYPE = "car";

    @Override
    public boolean canHandle(Load load) {
        return false;
    }
}
