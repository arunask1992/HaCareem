package com.careem.domain;

import com.careem.commons.BaseModel;
import com.careem.commons.BeanUtil;
import com.careem.domain.jackson.View;
import com.careem.domain.type.hibernate.HopStation;
import com.careem.domain.viewmodels.PartnerSchedule;
import com.careem.helpers.DateHelper;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
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
    @OneToOne
    protected HopStation destination;
    @JsonView(View.Schedule.class)
    protected Date estimatedTime;

    @JsonView(View.Schedule.class)
    @Setter
    protected String status;
    @OneToOne
    @JoinColumn(name = "quotation_id")
    protected Quotation quotation;

    @JsonView(View.Schedule.class)
    @Type(type = "com.careem.domain.type.hibernate.ModeOfTransportType")
    protected ModeOfTransport modeOfTransport;
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

    @SneakyThrows
    public void updateStatus(String status){
        this.status = status;
        String messageAsJSON = "{\"event\": \"quotation.status.changed\"" +
                "                \"quotation_id\":"  + this.quotation.getId() +
                "                \"status\":" + this.getStatus() +
                "  }";
        MessagePostProcessor messagePostProcessor = message -> message;
        BeanUtil.getBean(RabbitTemplate.class).convertAndSend("ecommerce_notification_queue", BeanUtil.getBean(ObjectMapper.class).writeValueAsBytes(messageAsJSON), messagePostProcessor);
    }

    public static List<Schedule> getAllDelayedDeliveries() {
       return entityManager()
                .createNativeQuery("select from schedule where (estimated_time IS NOT NULL AND CAST(estimated_time AS DATE) < "
                        + DateHelper.enclose(BeanUtil.getBean(DateHelper.class).today()) + ")", Schedule.class)
                .getResultList();

    }
}
