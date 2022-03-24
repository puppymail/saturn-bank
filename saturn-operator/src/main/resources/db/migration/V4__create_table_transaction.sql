CREATE TABLE transaction (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    account_src BIGINT,
    account_dst BIGINT,
    amount numeric(100,2),
    state int,
    date_time timestamp,
    PRIMARY KEY (id),
    FOREIGN KEY (account_src) REFERENCES account(id),
    FOREIGN KEY (account_dst) REFERENCES account(id)
);