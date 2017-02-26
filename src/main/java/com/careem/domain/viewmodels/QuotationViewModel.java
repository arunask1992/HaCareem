package com.careem.domain.viewmodels;

import com.careem.domain.GoodsType;
import com.careem.domain.Position;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationViewModel {
    protected String creatorId;
    protected Position source;
    protected Position destination;
    protected GoodsType typeOfGoods;
}
