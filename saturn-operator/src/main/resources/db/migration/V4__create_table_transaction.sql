CREATE TABLE transaction (
    transaction_id int UNIQUE NOT NULL,
    account_src int,
    account_dst int,
    amount numeric(100,2),
    state int,
    date_time date,
    PRIMARY KEY (transaction_id),
    FOREIGN KEY (account_src) REFERENCES account(id),
    FOREIGN KEY (account_dst) REFERENCES account(id)
);