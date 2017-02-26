package com.careem.domain;

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

    public List<Schedule> getHops() {
        Hub nearestHub = Hub.findNearestHub(quotation.getSource());
        ArrayList<Schedule> schedules = newArrayList();
        Map<Long, Double> costMap = nearestHub.getResources()
                .stream()
                .filter(resource -> resource.canHandle(quotation))
                .collect(Collectors.toMap(resource -> resource.getId(), resource -> Position.findDistance(resource.getLastKnownLocation(), quotation.getDestination())));
        final Long optimalResourceId = costMap.entrySet().stream().min(Map.Entry.comparingByValue(Double::compareTo)).get().getKey();
        Resource selectedResource = Resource.ACCESSOR.findIt(optimalResourceId);
        schedules.add(new Schedule(quotation, selectedResource, selectedResource.getLastKnownLocation()));
        return schedules;
    }
}
