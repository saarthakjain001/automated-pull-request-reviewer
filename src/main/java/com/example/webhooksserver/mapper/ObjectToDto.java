package com.example.webhooksserver.mapper;

public interface ObjectToDto<I, O> {
    boolean matches(String input);

    O convertToDto(String input);

}