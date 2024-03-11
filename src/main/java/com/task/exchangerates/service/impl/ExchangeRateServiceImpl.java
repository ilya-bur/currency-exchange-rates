package com.task.exchangerates.service.impl;

import com.task.exchangerates.dto.ExchangeRateDto;
import com.task.exchangerates.entity.Currency;
import com.task.exchangerates.entity.ExchangeRate;
import com.task.exchangerates.repository.CurrencyRepository;
import com.task.exchangerates.repository.ExchangeRateRepository;
import com.task.exchangerates.client.CurrencyExchangeRateClient;
import com.task.exchangerates.repository.InMemoryRepository;
import com.task.exchangerates.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.task.exchangerates.utils.ExchangeRateDtoConverter.convert;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final CurrencyExchangeRateClient exchangeRateClient;
    private final InMemoryRepository inMemoryRepository;
    private final ExchangeRateRepository rateRepository;
    private final CurrencyRepository currencyRepository;

    @Override
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void fetchRates() {
        Set<String> availableCurrencies = inMemoryRepository.getCurrencies();
        if(availableCurrencies.isEmpty()){
            log.warn("No currencies found in memory. Skipping rate fetching.");
            return;
        }
        List<ExchangeRateDto> exchangeRateDtoList = availableCurrencies.parallelStream()
                .map(exchangeRateClient::fetchExchangeRates)
                .toList();

        List<ExchangeRate> exchangeRates =  exchangeRateDtoList.stream()
                .flatMap(dto -> convert(dto).stream())
                .toList();

        Map<String, Map<String, Double>> exchangeRateMap = exchangeRateDtoList.stream()
                .collect(Collectors.toMap(ExchangeRateDto::getBase, ExchangeRateDto::getRates));

        inMemoryRepository.addExchangeRatesForAllCurrencies(exchangeRateMap);
        rateRepository.saveAll(exchangeRates);
    }

    @Override
    @Transactional(rollbackFor = {DataIntegrityViolationException.class, RuntimeException.class})
    public void saveCurrency(Currency currency) {
        try {
            String currencyCode = currency.getCurrency();
            currencyRepository.save(currencyCode);
            ExchangeRateDto exchangeRateDto = exchangeRateClient.fetchExchangeRates(currencyCode);
            inMemoryRepository.addExchangeRatesForSingleCurrency(currencyCode, exchangeRateDto.getRates());
            rateRepository.saveAll(convert(exchangeRateDto));
        } catch (DataIntegrityViolationException e) {
            log.error("Currency code {} already exists in the database ", currency, e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch rates for currency {}", currency, e);
            throw new RuntimeException("Failed to fetch rates for currency " + currency, e);
        }
    }
}
