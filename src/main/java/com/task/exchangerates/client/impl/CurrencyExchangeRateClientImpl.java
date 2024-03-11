package com.task.exchangerates.client.impl;

import com.task.exchangerates.dto.ExchangeRateDto;
import com.task.exchangerates.client.CurrencyExchangeRateClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeRateClientImpl implements CurrencyExchangeRateClient {

    @Value("${exchange.api.url}")
    private String apiUrl;
    @Value("${exchange.api.key}")
    private String apiId;

    private final RestTemplate restTemplate;

    /**
     * Fetches exchange rates for a specified currency from an external API.
     * This method retrieves exchange rates for the specified currency by making
     * a request to an external API. However, due to limitations of the external
     * service, it only allows fetching exchange rates for USD
     *
     * @param currency the currency for which exchange rates are to be fetched
     * @return the exchange rate data transfer object containing the retrieved exchange rates
     * @throws RuntimeException if an error occurs while fetching exchange rates from the API
     */
    @Override
    public ExchangeRateDto fetchExchangeRates(String currency) {

        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("app_id", apiId)
                .queryParam("base", "USD")
                .toUriString();

        try {
            ResponseEntity<ExchangeRateDto> responseEntity = restTemplate.getForEntity(url, ExchangeRateDto.class);
            ExchangeRateDto exchangeRateDto = responseEntity.getBody();
            if (exchangeRateDto != null) {
                exchangeRateDto.setBase(currency);
            }
            return exchangeRateDto;
        } catch (RestClientException e) {
            log.error("Error fetching exchange rates from API for base currency {}", currency, e);
            throw new RuntimeException("Failed to fetch exchange rates from API", e);
        }
    }
}
