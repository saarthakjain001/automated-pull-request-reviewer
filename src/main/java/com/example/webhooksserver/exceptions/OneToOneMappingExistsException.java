package com.example.webhooksserver.exceptions;

public class OneToOneMappingExistsException extends RuntimeException {
    public OneToOneMappingExistsException() {
        super("One To One Mapping Already Exists");
    }

}