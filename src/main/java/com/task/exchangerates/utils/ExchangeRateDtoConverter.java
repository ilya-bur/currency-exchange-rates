package com.task.exchangerates.utils;

import com.task.exchangerates.dto.ExchangeRateDto;
import com.task.exchangerates.entity.Currency;
import com.task.exchangerates.entity.ExchangeRate;

import java.util.List;

public class ExchangeRateDtoConverter {
    public static List<ExchangeRate> convert(ExchangeRateDto dto){
        return dto.getRates().entrySet().stream()
                .map(rate ->
                        new ExchangeRate(new Currency(dto.getBase()), dto.getTimestamp(),rate.getKey(), rate.getValue())
                )
                .toList();
    }
}
