ALTER TABLE bank_user ADD COLUMN password varchar(255) NOT NULL;
ALTER TABLE bank_user ADD CONSTRAINT user_unique_phone_number UNIQUE (phone_number);
ALTER TABLE bank_user ADD CONSTRAINT user_unique_email UNIQUE (email);
