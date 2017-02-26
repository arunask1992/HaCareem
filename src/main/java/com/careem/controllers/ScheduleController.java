package com.careem.controllers;

import com.careem.domain.Quotation;
import com.careem.domain.RouteTracer;
import com.careem.domain.Schedule;
import com.careem.domain.jackson.View;
import com.careem.domain.viewmodels.QuotationViewModel;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ScheduleController extends BaseController {
    @RequestMapping(value = "/ecommerce-api/request_quotation", method = POST)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.Schedule.class)
    public List<Schedule> getSchedule(@RequestBody QuotationViewModel quotation) {
        Quotation createdQuotation = Quotation.builder()
                .creatorId(quotation.getCreatorId())
                .source(quotation.getSource()).destination(quotation.getDestination())
                .typeOfGoods(quotation.getTypeOfGoods()).build();
        createdQuotation.persist();
        return new RouteTracer(createdQuotation).getHops();
    }
}
