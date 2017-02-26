package com.careem.domain.viewmodels;

import com.careem.domain.ModeOfTransport;
import com.careem.domain.Quotation;
import com.careem.domain.type.hibernate.HopStation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PartnerSchedule {
    Quotation quotation;
    String partnerName;
    HopStation source;
    HopStation destination;
    ModeOfTransport modeOfTransport;
    Date estimatedTime;
    String status;
}
