package com.saturn_bank.operator.exception;

public class EntityAlreadyPresentException extends Exception {

    public EntityAlreadyPresentException() {
        super();
    }

    public EntityAlreadyPresentException(String message) {
        super(message);
    }

    public EntityAlreadyPresentException(String message, Class<?> entityClass) {
        super(message + " - " + entityClass.getSimpleName());
    }

    public EntityAlreadyPresentException(String message, Class<?> entityClass, long entityId) {
        super(message + ", id: " + entityId + " - " + entityClass.getSimpleName());
    }

    public EntityAlreadyPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityAlreadyPresentException(Throwable cause) {
        super(cause);
    }

    public EntityAlreadyPresentException(String message, Class<?> entityClass, Throwable cause) {
        super(message + " - " + entityClass.getSimpleName(), cause);
    }

}
