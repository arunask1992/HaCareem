package com.careem.domain;

public class Bike extends Resource{
    public static final String TYPE = "bike";

    @Override
    public boolean canHandle(Load load) {
        return false;
    }
}
