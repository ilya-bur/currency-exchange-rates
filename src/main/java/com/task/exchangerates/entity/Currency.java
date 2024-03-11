package com.task.exchangerates.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "currency")
@Data
@NoArgsConstructor
public class Currency {

    @Id
    @Column(name = "currency")
    @NotBlank(message = "Currency code must not be blank")
    @Pattern(regexp = "[A-Z]{3}", message = "Currency code must consist of three uppercase letters")
    private String currency;

    @OneToMany(mappedBy = "baseCurrency", cascade = CascadeType.ALL)
    private Set<ExchangeRate> exchangeRates;

    public Currency(String currency) {
        this.currency = currency;
    }
}
