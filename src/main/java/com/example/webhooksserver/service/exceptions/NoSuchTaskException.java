package com.example.webhooksserver.service.exceptions;

public class NoSuchTaskException extends RuntimeException {
    public NoSuchTaskException(final String message) {
        super(message);
    }

    public NoSuchTaskException() {
        super("No Such Task Exception");
    }
}