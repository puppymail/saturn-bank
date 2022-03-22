CREATE TABLE card (
    account_id int,
    user_id int,
    percent numeric(8,5),
    type int,
    pincode int,
    FOREIGN KEY (user_id) REFERENCES bank_user(id),
    FOREIGN KEY (account_id) REFERENCES account(id)
);