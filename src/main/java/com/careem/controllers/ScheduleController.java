package com.careem.controllers;

import com.careem.domain.DispatchService;
import com.careem.domain.Quotation;
import com.careem.domain.Schedule;
import com.careem.domain.jackson.View;
import com.careem.domain.viewmodels.QuotationViewModel;
import com.careem.domain.viewmodels.StatusUpdateViewModel;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        return quotation.getTransferType().isWithinCity() ? new DispatchService(createdQuotation).getNativeHops() :
                new DispatchService(createdQuotation).getHops() ;
    }
    @RequestMapping(value = "/ecommerce-api/schedules/{scheduleId}/update_schedule_status", method = POST)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(View.Schedule.class)
    public Schedule getSchedule(@PathVariable Long scheduleId, @RequestBody StatusUpdateViewModel status) {
        final Schedule schedule = Schedule.getSchedule(scheduleId).get();
        schedule.updateStatus(status.getStatus().toString());
       return schedule.persist();
    }
}
