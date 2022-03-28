
CREATE TABLE "bank_user"
(
    "id" BIGINT GENERATED ALWAYS AS IDENTITY,
    "first_name" VARCHAR(255) NOT NULL,
    "middle_name" VARCHAR(255),
    "last_name" VARCHAR(255) NOT NULL,
    "birth_date" DATE NOT NULL,
    "registration_date" TIMESTAMP NOT NULL,
    "last_login" TIMESTAMP NOT NULL,
    "phone_number" VARCHAR(50) NOT NULL,
    "email" VARCHAR(255) NOT NULL,
    "type" INT NOT NULL,
    "role" INT NOT NULL,
    "is_deleted" BOOLEAN NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
)

