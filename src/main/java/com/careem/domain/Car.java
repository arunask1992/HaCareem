package com.careem.domain;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = Car.TYPE)
@NoArgsConstructor
public class Car extends Resource {
    public static final String TYPE = "car";

    public Car(String name, Hub hub, Position lastKnownPosition) {
        super(name, hub, lastKnownPosition);
    }

    @Override
    public boolean canHandle(Load load) {
        return false;
    }
}
