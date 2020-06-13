package com.example.webhooksserver.exceptions;

public class NoSuchRepositoryException extends RuntimeException {
    public NoSuchRepositoryException(final String message) {
        super(message);
    }

    public NoSuchRepositoryException() {
        super("No such Repository Exception");
    }
}