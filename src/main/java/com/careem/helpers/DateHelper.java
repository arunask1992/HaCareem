package com.careem.helpers;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateHelper {
    private Clock clock;

    public DateHelper() {
        setClock(Clock.systemDefaultZone());
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
    public static String enclose(Date date) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        return "'" + dateTimeFormat.format(date) + "'";
    }
    public Date today(){
        Instant instant = clock.instant();
        return Date.from(instant);
    }
}
