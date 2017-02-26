package com.careem.domain;

import com.careem.commons.BaseModel;
import com.careem.domain.jackson.View;
import com.careem.domain.type.hibernate.HopStation;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule")
@Getter
@Builder
public class Schedule extends BaseModel<Schedule> {
    public static Accessor<Schedule> ACCESSOR = new Accessor<>(Schedule.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Schedule.class)
    protected Long id;

    @OneToOne
    @JsonView(View.Schedule.class)
    protected HopStation source;

    @OneToOne
    @JsonView(View.Schedule.class)
    protected HopStation destination;
    @JsonView(View.Schedule.class)
    protected Date estimatedTime;

    @JsonView(View.Schedule.class)
    protected String status;
    @OneToOne
    protected Quotation quotation;
    public  Schedule (Quotation quotation, Resource resource, Position resourcelastSpotted){
        final Long stationId = new Random().nextLong();
        this.source = new HopStation(resource.getHub()).persist();
        this.destination = new HopStation(stationId.toString(), "end_user", "user_destination"+ stationId);
        this.estimatedTime = resource.getETA(quotation, resourcelastSpotted);
        this.status = "on_track";
    }
}
