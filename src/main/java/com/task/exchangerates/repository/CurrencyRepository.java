package com.task.exchangerates.repository;

import com.task.exchangerates.entity.Currency;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CurrencyRepository  extends JpaRepository<Currency, String> {

    /**
     * Saves a new currency in the database.
     *
     * @param currency The currency code to be saved.
     * @throws DataIntegrityViolationException If the specified currency code already exists in the database.
     */
    @Modifying
    @Query(value = "INSERT INTO currency (currency) VALUES (:currency)", nativeQuery = true)
    void save(String currency) throws DataIntegrityViolationException;
}
