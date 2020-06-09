package com.example.webhooksserver.ruleEngine;

public interface ObjectToDto<I, O> {
    boolean matches(String input);

    O convertToDto(String input);

}