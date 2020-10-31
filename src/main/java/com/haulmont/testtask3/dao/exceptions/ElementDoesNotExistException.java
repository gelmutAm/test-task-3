package com.haulmont.testtask3.dao.exceptions;

/**
 * Custom checked element does not exist exception.
 */
public class ElementDoesNotExistException extends Exception {

    public ElementDoesNotExistException() {
        super();
    }

    public ElementDoesNotExistException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }
}
