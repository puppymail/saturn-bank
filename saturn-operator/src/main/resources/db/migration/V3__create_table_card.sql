CREATE TABLE "card"
(
    "id" BIGINT GENERATED ALWAYS AS IDENTITY,
    "number" BIGINT NOT NULL,
    "user_id" BIGINT,
    "account_id" BIGINT,
    "percent" numeric(20,2),
    "type" int,
    "pincode" int,
    FOREIGN KEY (user_id) REFERENCES bank_user(id),
    FOREIGN KEY (account_id) REFERENCES account(id)
);
