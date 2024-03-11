package com.task.exchangerates.repository;

import java.util.Map;
import java.util.Set;

public interface InMemoryRepository {
    void addExchangeRatesForSingleCurrency(String currency, Map<String, Double> exchangeRates);
    void addExchangeRatesForAllCurrencies(Map<String, Map<String, Double>> exchangeRates);
    Map<String, Double> getExchangeRates(String currency);
    Set<String> getCurrencies();
}
