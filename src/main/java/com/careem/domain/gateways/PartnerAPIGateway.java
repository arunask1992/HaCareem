package com.careem.domain.gateways;

import com.careem.domain.ModeOfTransport;
import com.careem.domain.Quotation;
import com.careem.domain.type.hibernate.HopStation;
import com.careem.domain.viewmodels.PartnerSchedule;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Component
public class PartnerAPIGateway {
    public List<PartnerSchedule> getSchedule(Quotation quotation){
        //Mocked response
        return newArrayList(PartnerSchedule.builder()
                .quotation(quotation)
                .partnerName("Professional couriers")
                .modeOfTransport(new ModeOfTransport("train", "16789"))
                .source(new HopStation("S101", "external", "professionalCouriers"))
                .destination(new HopStation("dest11", "end_user", "dest11"))
                .estimatedTime(new Date(System.currentTimeMillis() + 50000000))
                .status("on_track")
                .build());

    }
}
