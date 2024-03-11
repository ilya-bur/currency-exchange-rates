package com.task.exchangerates.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exchange_rate")
@Data
@NoArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "currency", referencedColumnName = "currency")
    private Currency baseCurrency;

    private Long timestamp;

    @Column(name = "exchange_currency")
    private String currency;

    private Double rate;

    public ExchangeRate(Currency baseCurrency, Long timestamp, String currency, Double rate) {
        this.baseCurrency = baseCurrency;
        this.timestamp = timestamp;
        this.currency = currency;
        this.rate = rate;
    }
}
