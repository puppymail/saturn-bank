CREATE TABLE account_coin
(
    id INT GENERATED ALWAYS AS IDENTITY,
    currency VARCHAR(255),
    CONSTRAINT pk_account_coin PRIMARY KEY (id)
);

INSERT INTO account_coin(currency) VALUES ('RUB');
INSERT INTO account_coin(currency) VALUES ('EUR');
INSERT INTO account_coin(currency) VALUES ('USD');
