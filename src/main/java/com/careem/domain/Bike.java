package com.careem.domain;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = Bike.TYPE)
public class Bike extends Resource{
    public static final String TYPE = "bike";

    public Bike(String name, Hub hub, Position lastKnownPosition) {
        super(name, hub, lastKnownPosition);
    }

    @Override
    public boolean canHandle(Load load) {
        return false;
    }
}
