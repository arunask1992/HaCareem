package com.careem.domain;

import com.careem.commons.BaseModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.HOUR;
import static java.util.Calendar.MINUTE;

@Getter
@Entity
@Table(name = "resources")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = Bike.class, name = Bike.TYPE),
        @JsonSubTypes.Type(value = Car.class, name = Car.TYPE)
})
@NoArgsConstructor
@AllArgsConstructor
public abstract class Resource<T> extends BaseModel<Resource<T>> {
    public static Accessor<Resource> ACCESSOR = new Accessor<>(Resource.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String name;

    @ManyToOne
    protected Hub hub;

    @Type(type = "com.careem.domain.type.hibernate.PositionType")
    @Setter
    @Column(name = "last_known_location")
    private Position lastKnownLocation;

    public Resource(String name, Hub hub, Position lastKnownLocation) {
        this.name = name;
        this.hub = hub;
        this.lastKnownLocation = lastKnownLocation;
    }


    public static void updatePosition(long resourceId, Position position) {
        Resource.ACCESSOR.find(resourceId)
                .ifPresent(resource -> {
                        resource.setLastKnownLocation(position);
                        resource.persist();
                        resource.flush();
                });
    }

    public Long getId(){
        return this.id;
    }

    public abstract List<GoodsType> getHandledTypes();
    public boolean canHandle(Quotation quotation) {
        return getHandledTypes().contains(quotation.getTypeOfGoods());
    }
    public abstract Double getMaximumDeliveryTimePer100KmsInHrs();
    public Date getETA(Quotation quotation, Position lastKnownLocation){
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        Date oldDate = new Date();
        final long minutes = (long) (Position.findDistance(lastKnownLocation, quotation.getDestination()) / 100 * getMaximumDeliveryTimePer100KmsInHrs() * 60);
        new Date(oldDate.getTime() + minutes * MINUTE);
        return    cal.getTime();

    }

}
