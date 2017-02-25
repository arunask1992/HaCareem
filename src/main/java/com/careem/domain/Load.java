package com.careem.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Load {
    private int quantity;
    private String unit;
    private GoodsType type;
}
