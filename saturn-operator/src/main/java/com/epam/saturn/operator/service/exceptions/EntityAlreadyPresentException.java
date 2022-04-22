package com.epam.saturn.operator.service.exceptions;

public class EntityAlreadyPresentException extends RuntimeException {

    public EntityAlreadyPresentException() {
        super();
    }

    public EntityAlreadyPresentException(String message) {
        super(message);
    }

    public EntityAlreadyPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityAlreadyPresentException(Throwable cause) {
        super(cause);
    }

}
