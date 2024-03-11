package com.task.exchangerates.service;

import com.task.exchangerates.entity.Currency;

public interface ExchangeRateService {
    void fetchRates();
    void saveCurrency(Currency currency);
}
