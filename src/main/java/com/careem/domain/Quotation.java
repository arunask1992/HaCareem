package com.careem.domain;

import com.careem.commons.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quotations")
@Getter
@Builder
public class Quotation extends BaseModel<Quotation>{
    public static Accessor<Resource> ACCESSOR = new Accessor<>(Resource.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Wither
    protected String creatorId;

    @Type(type = "com.careem.domain.type.hibernate.PositionType")
    @Wither
    protected Position source;

    @Type(type = "com.careem.domain.type.hibernate.PositionType")
    @Wither
    protected Position destination;

    protected Date expectedTimeOfDelivery;

    @Type(type = "com.careem.domain.type.hibernate.PositionType")
    protected GoodsType typeOfGoods;
}
