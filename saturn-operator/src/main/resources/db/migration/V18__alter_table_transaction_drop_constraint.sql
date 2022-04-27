ALTER TABLE transaction DROP CONSTRAINT transaction_account_src_fkey;
ALTER TABLE transaction DROP CONSTRAINT transaction_account_dst_fkey;
ALTER TABLE transaction ALTER COLUMN account_src TYPE VARCHAR(20);
ALTER TABLE transaction ALTER COLUMN account_dst TYPE VARCHAR(20);