package com.careem.domain;

import com.careem.commons.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hubs")
public class Hub extends BaseModel<Hub> {
    private static Accessor<Hub> ACCESSOR = new Accessor<>(Hub.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Type(type = "com.careem.domain.type.hibernate.PositionType")
    private Position position;
}
