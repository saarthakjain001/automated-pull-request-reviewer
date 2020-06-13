package com.example.webhooksserver.exceptions;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException(final String message) {
        super(message);
    }

    public NotImplementedException() {
        super("Not implemented this functionality");
    }
}
