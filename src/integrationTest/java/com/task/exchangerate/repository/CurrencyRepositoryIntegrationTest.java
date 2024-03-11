package com.task.exchangerate.repository;

import com.task.exchangerate.ContainersEnvironment;
import com.task.exchangerates.ExchangeRatesApplication;
import com.task.exchangerates.entity.Currency;
import com.task.exchangerates.repository.CurrencyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExchangeRatesApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyRepositoryIntegrationTest extends ContainersEnvironment {
    @Autowired
    private CurrencyRepository currencyRepository;

    @AfterEach
    void afterEach(){
        currencyRepository.deleteAll();
    }

    @Test
    void testSave(){
        Currency currency = new Currency("USD");

        currencyRepository.save(currency);
        Currency persistedCurrency = currencyRepository.findAll().get(0);

        Assertions.assertNotNull(persistedCurrency.getCurrency());
        Assertions.assertEquals("USD", persistedCurrency.getCurrency());
    }

    @Test
    void testSaveByCurrencyCodeSuccessfully(){

        currencyRepository.save("USD");
        Currency persistedCurrency = currencyRepository.findAll().get(0);

        Assertions.assertNotNull(persistedCurrency.getCurrency());
        Assertions.assertEquals("USD", persistedCurrency.getCurrency());
    }

    @Test
    void testSaveByCurrencyCodeFailed(){
        Currency currency = new Currency("USD");
        currencyRepository.save(currency);

        DataIntegrityViolationException exception = Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                currencyRepository.save("USD")
        );

        String expectedMessage = "violates unique constraint";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }
}

