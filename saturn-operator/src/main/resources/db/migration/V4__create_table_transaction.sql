CREATE TABLE transaction (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    account_src BIGINT,
    account_dst BIGINT,
    amount numeric(100,2),
    purpose varchar,
    state int,
    date_time timestamp,
    CONSTRAINT pk_transaction PRIMARY KEY (id),
    CONSTRAINT fk_transaction_account_src FOREIGN KEY (account_src) REFERENCES account(id),
    CONSTRAINT fk_transaction_account_dst FOREIGN KEY (account_dst) REFERENCES account(id)
);
