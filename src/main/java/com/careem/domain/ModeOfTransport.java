package com.careem.domain;

import com.careem.domain.jackson.PersistentEnumSerializer;
import com.careem.domain.jackson.View;
import com.careem.domain.type.hibernate.PersistentEnum;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeOfTransport {
    @JsonView(View.Schedule.class)
    public String type;
    @JsonView(View.Schedule.class)
    public String name;
}
