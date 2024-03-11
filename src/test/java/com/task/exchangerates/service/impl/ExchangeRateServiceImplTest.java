package com.task.exchangerates.service.impl;

import com.task.exchangerates.client.CurrencyExchangeRateClient;
import com.task.exchangerates.dto.ExchangeRateDto;
import com.task.exchangerates.entity.Currency;
import com.task.exchangerates.repository.CurrencyRepository;
import com.task.exchangerates.repository.ExchangeRateRepository;
import com.task.exchangerates.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceImplTest {
    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @Mock
    private CurrencyExchangeRateClient exchangeRateClient;

    @Mock
    private InMemoryRepository inMemoryRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Test
    void testFetchRates() {
        Set<String> availableCurrencies = new HashSet<>(Arrays.asList("USD", "EUR"));

        ExchangeRateDto exchangeRateDto1 =  ExchangeRateDto.builder()
                .base("USD")
                .rates(Map.of("EUR", 0.9))
                .build();

        ExchangeRateDto exchangeRateDto2 =  ExchangeRateDto.builder()
                .base("EUR")
                .rates(Map.of("USD", 0.9))
                .build();

        when(inMemoryRepository.getCurrencies()).thenReturn(availableCurrencies);
        when(exchangeRateClient.fetchExchangeRates(Mockito.eq("USD"))).thenReturn(exchangeRateDto1);
        when(exchangeRateClient.fetchExchangeRates(Mockito.eq("EUR"))).thenReturn(exchangeRateDto2);

        exchangeRateService.fetchRates();

        verify(inMemoryRepository).addExchangeRatesForAllCurrencies(anyMap());
        verify(exchangeRateRepository).saveAll(anyList());
    }

    @Test
    void testSaveCurrencySuccess() {
        Currency currency = new Currency("USD");

        ExchangeRateDto exchangeRateDto =  ExchangeRateDto.builder()
                .base("USD")
                .rates(Map.of("EUR", 0.9))
                .build();

        doNothing().when(currencyRepository).save(anyString());
        when(exchangeRateClient.fetchExchangeRates(anyString())).thenReturn(exchangeRateDto);
        doNothing().when(inMemoryRepository).addExchangeRatesForSingleCurrency(anyString(), anyMap());
        when(exchangeRateRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> exchangeRateService.saveCurrency(currency));

        verify(currencyRepository).save(anyString());
        verify(inMemoryRepository).addExchangeRatesForSingleCurrency(anyString(), anyMap());
        verify(exchangeRateRepository).saveAll(anyList());
    }

    @Test
    void testSaveCurrencyDuplicateCurrency() {
        Currency currency = new Currency("USD");

        doThrow(DataIntegrityViolationException.class).when(currencyRepository).save(anyString());

        assertThrows(DataIntegrityViolationException.class, () -> exchangeRateService.saveCurrency(currency));
    }

    @Test
    void testSaveCurrencyFailure() {
        Currency currency = new Currency("USD");

        when(currencyRepository.save(any(Currency.class))).thenReturn(currency);
        when(exchangeRateClient.fetchExchangeRates(anyString())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> exchangeRateService.saveCurrency(currency));
    }
}
