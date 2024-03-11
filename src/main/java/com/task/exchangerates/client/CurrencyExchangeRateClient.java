package com.task.exchangerates.client;

import com.task.exchangerates.dto.ExchangeRateDto;

public interface CurrencyExchangeRateClient {
    ExchangeRateDto fetchExchangeRates(String currency);
}
