package com.careem.domain;

import com.careem.commons.BaseModel;
import com.careem.domain.jackson.View;
import com.careem.domain.type.hibernate.HopStation;
import com.careem.domain.viewmodels.PartnerSchedule;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;
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

    @JsonView(View.Schedule.class)
    @Type(type = "com.careem.domain.type.hibernate.ModeOfTransportType")
    protected ModeOfTransport modeOfTransport;

    @JsonView(View.Schedule.class)
    @OneToOne
    protected HopStation destination;
    @JsonView(View.Schedule.class)
    protected Date estimatedTime;

    @JsonView(View.Schedule.class)
    @Setter
    protected String status;
    @OneToOne
    protected Quotation quotation;
    public  Schedule (Quotation quotation, Resource resource, Position resourcelastSpotted){
        final Long stationId = new Random().nextLong();
        this.quotation = quotation;
        this.source = new HopStation(resource.getHub()).persist();
        this.destination = new HopStation(stationId.toString(), "end_user", "user_destination"+ stationId).persist();
        this.estimatedTime = resource.getETA(quotation, resourcelastSpotted);
        this.modeOfTransport = new ModeOfTransport("hub", resource.getName());
        this.status = "on_track";
    }
    public Schedule(PartnerSchedule partnerSchedule){
        this.quotation = partnerSchedule.getQuotation();
        this.source = partnerSchedule.getSource().persist();
        this.destination = partnerSchedule.getDestination().persist();
        this.estimatedTime = partnerSchedule.getEstimatedTime();
        this.modeOfTransport = partnerSchedule.getModeOfTransport();
        this.status = partnerSchedule.getStatus();
    }

    public static Optional<Schedule> getSchedule(Long scheduleId) {
        return Schedule.ACCESSOR.find(scheduleId);
    }
}
