CREATE TABLE account (
    account_id int UNIQUE NOT NULL,
    is_default boolean,
    amount numeric(100,2),
    percent numeric(8,5),
    type int,
    owner_id int,
    PRIMARY KEY (account_id),
    FOREIGN KEY (owner_id) REFERENCES bank_user(user_id)
);