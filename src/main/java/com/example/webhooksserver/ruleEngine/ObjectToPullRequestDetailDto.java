package com.example.webhooksserver.ruleEngine;

import com.example.webhooksserver.domain.PayloadEntity;
import com.example.webhooksserver.domain.PullRequestDetail;
import com.example.webhooksserver.dtos.PayloadDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.gitUtils.enums.GitEvents;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectToPullRequestDetailDto implements ObjectToDto<String, PayloadDto> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean matches(String input) {
        return input.equals(GitEvents.pull_request.toString());
    }

    @Override
    public PullRequestDetailDto convertToDto(String input) {
        // ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(input, PullRequestDetailDto.class);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}