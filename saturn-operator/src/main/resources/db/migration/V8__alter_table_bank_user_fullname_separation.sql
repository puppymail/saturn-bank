ALTER TABLE bank_user DROP COLUMN IF EXISTS full_name;
ALTER TABLE bank_user ADD COLUMN first_name VARCHAR(255) NOT NULL;
ALTER TABLE bank_user ADD COLUMN middle_name VARCHAR(255);
ALTER TABLE bank_user ADD COLUMN last_name VARCHAR(255) NOT NULL;
