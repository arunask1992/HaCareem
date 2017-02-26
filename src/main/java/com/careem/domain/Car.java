package com.careem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

import static com.careem.domain.GoodsType.*;
import static com.google.common.collect.Lists.newArrayList;

@Entity
@DiscriminatorValue(value = Car.TYPE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Car extends Resource {
    public static final String TYPE = "car";
    @JsonIgnore
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
