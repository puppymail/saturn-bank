CREATE TABLE bank_user(
    user_id int UNIQUE NOT NULL,
    full_name varchar(255),
    phone_number varchar(255),
    reg_info varchar(255),
    role int,
    type int,
    PRIMARY KEY (user_id)
);