package com.careem.domain;

import com.careem.commons.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Wither;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quotations")
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quotation extends BaseModel<Quotation>{
    public static Accessor<Resource> ACCESSOR = new Accessor<>(Resource.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Wither
    public String creatorId;

    @Type(type = "com.careem.domain.type.hibernate.PositionType")
    @Wither
    public Position source;

    @Type(type = "com.careem.domain.type.hibernate.PositionType")
    @Wither
    public Position destination;

    public Date expectedTimeOfDelivery;

    @Type(type = "com.careem.domain.type.hibernate.GoodsType")
    @JsonIgnore
    public GoodsType typeOfGoods;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "quotation")
    public List<Schedule> schedules;

    @Setter
    public String status;

    public void markDelivered() {
        this.status = "delivered";
    }
}
