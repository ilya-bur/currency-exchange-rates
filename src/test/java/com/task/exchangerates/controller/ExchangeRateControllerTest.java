package com.task.exchangerates.controller;

import com.task.exchangerates.repository.InMemoryRepository;
import com.task.exchangerates.service.ExchangeRateService;
import com.task.exchangerates.entity.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateControllerTest {
    @Mock
    private InMemoryRepository inMemoryRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ExchangeRateController controller;

    @Test
    void testGetExchangeRates() {
        String currency = "USD";
        Map<String, Double> exchangeRates = Collections.singletonMap("EUR", 0.85);
        when(inMemoryRepository.getExchangeRates(currency)).thenReturn(exchangeRates);

        Map<String, Double> result = controller.getExchangeRates(currency);

        assertEquals(exchangeRates, result);
        verify(inMemoryRepository).getExchangeRates(currency);
    }

    @Test
    void testGetExchangeRatesNotFound() {
        String currency = "XXX";
        when(inMemoryRepository.getExchangeRates(currency)).thenReturn(Collections.emptyMap());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.getExchangeRates(currency));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testGetCurrencies() {
        Set<String> currencies = Set.of("USD", "EUR", "GBP");
        when(inMemoryRepository.getCurrencies()).thenReturn(currencies);

        Set<String> result = controller.getCurrencies();

        assertEquals(currencies, result);
        verify(inMemoryRepository).getCurrencies();
    }

    @Test
    void testAddCurrency() {
        Currency currency = new Currency("USD");
        controller.addCurrency(currency);

        verify(exchangeRateService).saveCurrency(currency);
    }

    @Test
    void testAddCurrencyConflict() {
        Currency currency = new Currency("USD");
        doThrow(DataIntegrityViolationException.class).when(exchangeRateService).saveCurrency(currency);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.addCurrency(currency));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }
}
