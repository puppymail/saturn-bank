CREATE TABLE card
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    number BIGINT NOT NULL,
    user_id BIGINT,
    account_id BIGINT,
    percent numeric(20,2),
    type int,
    pincode int,
    cvv2 int,
    CONSTRAINT pk_card PRIMARY KEY (id),
    CONSTRAINT fk_card_user FOREIGN KEY (user_id) REFERENCES bank_user(id),
    CONSTRAINT fk_card_account FOREIGN KEY (account_id) REFERENCES account(id)
);
