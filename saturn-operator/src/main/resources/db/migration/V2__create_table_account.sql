
CREATE TABLE account
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    number BIGINT NOT NULL,
    is_default boolean,
    user_id BIGINT,
    type INT,
    percent numeric(10,2),
    amount numeric(100,2),
    account_coin INT NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (id),
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES bank_user(id)
)

