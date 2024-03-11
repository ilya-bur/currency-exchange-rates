package com.task.exchangerates.repository.impl;

import com.task.exchangerates.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryExchangeRateRepositoryTest {

    private InMemoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryExchangeRateRepository();
    }

    @Test
    void testAddExchangeRatesForSingleCurrency() {
        String currency = "USD";
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.85);

        repository.addExchangeRatesForSingleCurrency(currency, rates);

        assertEquals(rates, repository.getExchangeRates(currency));
    }

    @Test
    void testAddExchangeRatesForAllCurrencies() {
        Map<String, Map<String, Double>> currencyToRatesMap = new HashMap<>();
        Map<String, Double> rates1 = new HashMap<>();
        rates1.put("EUR", 0.85);
        currencyToRatesMap.put("USD", rates1);

        repository.addExchangeRatesForAllCurrencies(currencyToRatesMap);

        assertEquals(rates1, repository.getExchangeRates("USD"));
    }

    @Test
    void testGetExchangeRatesNotFound() {
        assertEquals(Collections.emptyMap(), repository.getExchangeRates("XYZ"));
    }

    @Test
    void testGetCurrencies() {
        repository.addExchangeRatesForSingleCurrency("USD", Collections.emptyMap());
        repository.addExchangeRatesForSingleCurrency("EUR", Collections.emptyMap());

        Set<String> expectedCurrencies = Set.of("USD", "EUR");

        assertEquals(expectedCurrencies, repository.getCurrencies());
    }
}
