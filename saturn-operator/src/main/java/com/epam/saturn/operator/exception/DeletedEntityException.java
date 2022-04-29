package com.epam.saturn.operator.exception;

public class DeletedEntityException extends IllegalArgumentException {

    public DeletedEntityException() {
        super();
    }

    public DeletedEntityException(String s) {
        super(s);
    }

    public DeletedEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletedEntityException(Throwable cause) {
        super(cause);
    }

}
