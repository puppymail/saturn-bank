CREATE TABLE course
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    char_code VARCHAR(255),
    name VARCHAR(255),
    nominal INTEGER,
    num_code VARCHAR(255),
    value NUMERIC NOT NULL
);
