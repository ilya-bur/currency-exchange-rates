package com.task.exchangerate.repository;

import com.task.exchangerate.ContainersEnvironment;
import com.task.exchangerates.ExchangeRatesApplication;
import com.task.exchangerates.entity.Currency;
import com.task.exchangerates.entity.ExchangeRate;
import com.task.exchangerates.repository.CurrencyRepository;
import com.task.exchangerates.repository.ExchangeRateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExchangeRatesApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExchangeRateRepositoryIntegrationTest extends ContainersEnvironment {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    @AfterEach
    void afterEach(){
        exchangeRateRepository.deleteAll();
        currencyRepository.deleteAll();
    }

    @Test
    void testSave(){

        Currency currency = new Currency("USD");
        currencyRepository.save(currency);

        ExchangeRate exchangeRate = new ExchangeRate(currency, 5454L,"EUR", 0.9);
        exchangeRateRepository.save(exchangeRate);

        Currency persistedCurrency = currencyRepository.findAll().get(0);
        ExchangeRate persistedExchangeRate = exchangeRateRepository.findAll().get(0);
        Assertions.assertNotNull(persistedExchangeRate.getId());
        Assertions.assertNotNull(persistedCurrency.getCurrency());
        Assertions.assertEquals(persistedExchangeRate.getCurrency(), "EUR");
        Assertions.assertEquals(persistedCurrency.getCurrency(), "USD");
    }
}

