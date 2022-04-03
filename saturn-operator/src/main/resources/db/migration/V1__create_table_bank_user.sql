
CREATE TABLE bank_user
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    full_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    registration_date TIMESTAMP NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    type INT NOT NULL,
    role INT NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
)
