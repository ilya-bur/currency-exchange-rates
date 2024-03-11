package com.task.exchangerates.controller;

import com.task.exchangerates.repository.InMemoryRepository;
import com.task.exchangerates.service.ExchangeRateService;
import com.task.exchangerates.entity.Currency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("app/v1")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final InMemoryRepository inMemoryRepository;
    private final ExchangeRateService exchangeRateService;
    @GetMapping("/exchange-rates/{currency}")
    public Map<String, Double> getExchangeRates(@PathVariable @Pattern(regexp = "[A-Z]{3}") String currency){
        Map<String, Double> exchangeRates = inMemoryRepository.getExchangeRates(currency);
        if (exchangeRates == null || exchangeRates.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Exchange rates not found for currency: " + currency));
        }
        return exchangeRates;
    }
    @GetMapping("/currencies")
    public Set<String> getCurrencies(){
        return inMemoryRepository.getCurrencies();
    }

    @PostMapping( value = "/currencies", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addCurrency(@Valid @RequestBody Currency currency){
        try {
            exchangeRateService.saveCurrency(currency);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Currency already exists", e);
        }
    }
}
