CREATE TABLE user_type
(
    id INT GENERATED ALWAYS AS IDENTITY,
    type VARCHAR(255),
    CONSTRAINT pk_user_type PRIMARY KEY (id)
);

INSERT INTO user_type(type) VALUES ('CLIENT');
INSERT INTO user_type(type) VALUES ('EMPLOYEE');
