package com.careem.domain;

import com.careem.commons.BeanUtil;
import com.careem.domain.gateways.PartnerAPIGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

public class RouteTracer {
    private Quotation quotation;

    public RouteTracer(Quotation quotation) {
        this.quotation = quotation;
    }

    public List<Schedule> getNativeHops() {
        Hub nearestHub = Hub.findNearestHub(quotation.getSource());
        ArrayList<Schedule> schedules = newArrayList();
        Long optimalResourceId = getOptimalResourceId(nearestHub);
        Resource.ACCESSOR.find(optimalResourceId).ifPresent(selectedResource -> {
            schedules.add(new Schedule(quotation, selectedResource, selectedResource.getLastKnownLocation()).persist());
        });
        if(optimalResourceId == null) {
            //Retry with other hubs
            int i=0;
            List<Hub> hubs = Hub.returnOrderedHubs(quotation.getSource());
            do {
                optimalResourceId = getOptimalResourceId(hubs.get(i));
                i++;
            } while (optimalResourceId != null || i!=hubs.size());
            Resource.ACCESSOR.find(optimalResourceId).ifPresent(selectedResource -> {
                schedules.add(new Schedule(quotation, selectedResource, selectedResource.getLastKnownLocation()).persist());
            });
        }
        return schedules;
    }

    private Long getOptimalResourceId(Hub nearestHub) {
        Map<Long, Double> costMap = nearestHub.getResources()
                .stream()
                .filter(resource -> resource.canHandle(quotation))
                .collect(Collectors.toMap(resource -> resource.getId(), resource -> Position.findDistance(resource.getLastKnownLocation(), quotation.getDestination())));
        return costMap.entrySet().stream().min(Map.Entry.comparingByValue(Double::compareTo)).get().getKey();
    }

    public List<Schedule> getHops() {
        List<Schedule> arrivedSchedule = getNativeHops();
        List<Schedule> partnerSchedules = BeanUtil.getBean(PartnerAPIGateway.class)
                .getSchedule(quotation).stream().map(partnerSchedule -> new Schedule(partnerSchedule).persist()).collect(Collectors.toList());
        arrivedSchedule.addAll(partnerSchedules);
        return arrivedSchedule;
    }
}
