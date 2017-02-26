package com.careem.scheduler;

import com.careem.commons.BeanUtil;
import com.careem.commons.DBContextProvider;
import com.careem.domain.Schedule;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleStatusTracker implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    DBContextProvider contextProvider;

    @Scheduled(cron = "${scheduler.track-status.cron}")
    @SneakyThrows
    public void trackStatus() {
        contextProvider.withDBConnection(() -> {
            Schedule.getAllDelayedDeliveries().forEach(schedule -> {
                String messageAsJSON = "{\"event\": \"schedule.status.delayed\"" +
                        "                \"quotation_id\":"  + schedule.getQuotation().getId()  +
                        "  }";
                MessagePostProcessor messagePostProcessor = message -> message;
                BeanUtil.getBean(RabbitTemplate.class).convertAndSend("ecommerce_notification_queue", BeanUtil.getBean(ObjectMapper.class).writeValueAsBytes(messageAsJSON), messagePostProcessor);
            });
        });
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.trackStatus();
    }

}

