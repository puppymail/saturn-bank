
CREATE TABLE "account"
(
    "id" BIGINT GENERATED ALWAYS AS IDENTITY,
    "user_id" BIGINT,
    "is_default" boolean,
    "amount" numeric(100,2),
    "percent" numeric(8,5),
    "type" INT,
    CONSTRAINT pk_account PRIMARY KEY (id),
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES bank_user(id)
)
