package com.careem.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

import static com.careem.domain.GoodsType.*;
import static com.careem.domain.GoodsType.LIQUID;
import static com.google.common.collect.Lists.newArrayList;

@Entity
@DiscriminatorValue(value = Bike.TYPE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bike extends Resource{
    public static final String TYPE = "bike";

    @JsonIgnore
    public static final List<GoodsType> handledTypes = newArrayList(GENERAL);

    public Bike(String name, Hub hub, Position lastKnownPosition) {
        super(name, hub, lastKnownPosition);
    }

    @Override
    public List<GoodsType> getHandledTypes() {
        return handledTypes;
    }

    @Override
    public Double getMaximumDeliveryTimePer100KmsInHrs() {
        return 0.45;
    }
}
