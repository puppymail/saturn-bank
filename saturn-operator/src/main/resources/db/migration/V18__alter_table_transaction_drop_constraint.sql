ALTER TABLE transaction DROP CONSTRAINT fk_transaction_account_src;
ALTER TABLE transaction DROP CONSTRAINT fk_transaction_account_dst;
ALTER TABLE transaction ALTER COLUMN account_src TYPE VARCHAR(20);
ALTER TABLE transaction ALTER COLUMN account_dst TYPE VARCHAR(20);
