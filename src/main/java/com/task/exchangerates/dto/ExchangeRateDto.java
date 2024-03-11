package com.task.exchangerates.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
public class ExchangeRateDto {

    private Long timestamp;
    private String base;
    private Map<String, Double> rates;
}
