
-- Create currency table
CREATE TABLE currency (
    currency VARCHAR(255) PRIMARY KEY
);

-- Create exchange_rate table
CREATE TABLE exchange_rate (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    currency VARCHAR(255) NOT NULL,
    timestamp BIGINT NOT NULL,
    exchange_currency VARCHAR(255),
    rate DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (currency) REFERENCES currency(currency)
);