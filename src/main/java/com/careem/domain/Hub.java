package com.careem.domain;

import com.careem.commons.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "hub")
    private List<Resource> resources;

    public static Hub findNearestHub(Position source) {
        final List<Hub> hubs = Hub.ACCESSOR.all();
        Hub nearestHub = hubs.get(0);
        final double initialDistance = Position.findDistance(nearestHub.getPosition(), source);
        for(Hub hub : hubs){
            if(Position.findDistance(hub.getPosition(), source) < initialDistance)
                nearestHub = hub;
        }
        return nearestHub;
    }
    public static List<Hub> returnOrderedHubs(Position source){
        final List<Hub> hubs = Hub.ACCESSOR.all();
        Collections.sort(hubs, (h1, h2) -> Position.findDistance(h1.getPosition(), source).compareTo(Position.findDistance(h2.getPosition(), source)));
        return hubs;
    }
}
