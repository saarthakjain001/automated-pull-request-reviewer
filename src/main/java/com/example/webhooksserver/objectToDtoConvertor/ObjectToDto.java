package com.example.webhooksserver.objectToDtoConvertor;

public interface ObjectToDto<I, O> {
    boolean matches(String input);

    O convertToDto(String input);

}