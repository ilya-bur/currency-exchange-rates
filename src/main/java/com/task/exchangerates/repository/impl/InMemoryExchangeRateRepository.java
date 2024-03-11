package com.task.exchangerates.repository.impl;

import com.task.exchangerates.repository.InMemoryRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryExchangeRateRepository implements InMemoryRepository {

    private final Map<String, Map<String, Double>> exchangeRateMap;

    public InMemoryExchangeRateRepository() {
        this.exchangeRateMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addExchangeRatesForSingleCurrency(String currency, Map<String, Double> rates) {
        exchangeRateMap.put(currency, rates);
    }

    @Override
    public void addExchangeRatesForAllCurrencies(Map<String, Map<String, Double>> currencyToRatesMap) {
        exchangeRateMap.putAll(currencyToRatesMap);
    }

    @Override
    public Map<String, Double> getExchangeRates(String currency) {
        Map<String, Double> exchangeRate = exchangeRateMap.getOrDefault(currency, Collections.emptyMap());
        return Collections.unmodifiableMap(exchangeRate);
    }

    @Override
    public Set<String> getCurrencies() {
        return exchangeRateMap.keySet();
    }
}
