package com.careem.domain;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

import static com.careem.domain.GoodsType.*;
import static com.google.common.collect.Lists.newArrayList;

@Entity
@DiscriminatorValue(value = Car.TYPE)
@NoArgsConstructor
public class Car extends Resource {
    public static final String TYPE = "car";
    public static final List<GoodsType> handledTypes = newArrayList(ELECTRICAL_APPLICANCE, PERISHABLE_GOODS, GENERAL, SAFE_DELIVERY, LIQUID);

    public Car(String name, Hub hub, Position lastKnownPosition) {
        super(name, hub, lastKnownPosition);
    }

    @Override
    public List<GoodsType> getHandledTypes() {
        return handledTypes;
    }

    @Override
    public Double getMaximumDeliveryTimePer100KmsInHrs() {
        return 1.0;
    }

}
