CREATE TABLE user_role
(
    id INT GENERATED ALWAYS AS IDENTITY,
    role VARCHAR(255),
    CONSTRAINT pk_user_role PRIMARY KEY (id)
);

INSERT INTO user_role(role) VALUES ('CLIENT');
INSERT INTO user_role(role) VALUES ('OPERATOR');
INSERT INTO user_role(role) VALUES ('ADMIN');
INSERT INTO user_role(role) VALUES ('ATM');
