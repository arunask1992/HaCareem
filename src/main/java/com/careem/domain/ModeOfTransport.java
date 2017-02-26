package com.careem.domain;

import com.careem.domain.jackson.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeOfTransport {
    @JsonView(View.Schedule.class)
    String type;
    @JsonView(View.Schedule.class)
    String name;
}
