package com.saturn_bank.operator.exception;

import lombok.Getter;

@Getter
public class NoSuchEntityException extends Exception {

    public NoSuchEntityException() {
        super();
    }

    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(String message, Class<?> entityClass) {
        super(message + " - " + entityClass.getSimpleName());
    }

    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchEntityException(String message, Class<?> entityClass, Throwable cause) {
        super(message + " - " + entityClass.getSimpleName(), cause);
    }

}
