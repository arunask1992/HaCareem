package com.careem.domain;

import com.careem.commons.BaseModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "resources")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = Bike.class, name = Bike.TYPE),
        @JsonSubTypes.Type(value = Car.class, name = Car.TYPE)
})
public abstract class Resource<T> extends BaseModel<Resource<T>> {
    public static Accessor<Resource> ACCESSOR = new Accessor<>(Resource.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String name;

    @ManyToOne
    protected Hub hub;

    @Type(type = "com.careem.domain.type.hibernate.PositionType")
    private Position lastKnownPosition;

    public abstract boolean canHandle(Load load);
}
