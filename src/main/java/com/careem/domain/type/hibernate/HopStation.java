package com.careem.domain.type.hibernate;

import com.careem.commons.BaseModel;
import com.careem.domain.Hub;
import com.careem.domain.Position;
import com.careem.domain.jackson.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Random;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hop_station")
@Getter
public class HopStation extends BaseModel<HopStation> {
    public static Accessor<HopStation> ACCESSOR = new Accessor<>(HopStation.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @JsonView(View.Schedule.class)
    protected String stationId;

    @JsonView(View.Schedule.class)
    protected String type;

    @JsonView(View.Schedule.class)
    protected String stationName;
    public HopStation(Hub hub){
        this.stationId = hub.getId().toString();
        this.type = "hub";
        this.stationName = hub.getName();
    }

    public HopStation(String stationId, String type, String stationName){
        this.stationId = stationId;
        this.type = type;
        this.stationName = stationName;
    }

    public static HopStation createEndUserDestination(){
        final Long stationId = new Random().nextLong();
        return new HopStation(stationId.toString(), "end_user", "user_destination"+ stationId);
    }
}
