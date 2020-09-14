package com.example.webhooksserver.objectToDtoConvertor;

import com.example.webhooksserver.dtos.PayloadDto;
import com.example.webhooksserver.dtos.PushDetailDto;
import com.example.webhooksserver.enums.GitEvents;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectToPushDetailDto implements ObjectToDto<String, PayloadDto> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean matches(String input) {
        return input.equals(GitEvents.PUSH.getEvent());
    }

    @Override
    public PushDetailDto convertToDto(String input) {
        try {
            return objectMapper.readValue(input, PushDetailDto.class);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}