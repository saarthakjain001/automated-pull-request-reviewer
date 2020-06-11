package com.example.webhooksserver.ruleEngine;

import java.util.ArrayList;
import java.util.List;

import com.example.webhooksserver.dtos.PayloadDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectToDtoRuleEngine {

    private final ObjectToPullRequestDetailDto objToPullRequestDetailDto;

    private final ObjectToPushDetailDto objToPushDetailDto;

    List<ObjectToDto<String, PayloadDto>> rules;

    @Autowired
    public ObjectToDtoRuleEngine(ObjectToPullRequestDetailDto objToPullRequestDetailDto,
            ObjectToPushDetailDto objToPushDetailDto) {
        rules = new ArrayList<>();
        this.objToPullRequestDetailDto = objToPullRequestDetailDto;
        this.objToPushDetailDto = objToPushDetailDto;
        rules.add(objToPullRequestDetailDto);
        rules.add(objToPushDetailDto);
        // this.registerRule(objToPullRequestDetailDto);
        // this.registerRule(objToPushDetailDto);

    }

    public PayloadDto rule(String event, String payload) {
        return rules.stream().filter(rule -> rule.matches(event)).map(rule -> rule.convertToDto(payload)).findFirst()
                .orElseThrow(() -> new RuntimeException("No Matching rule found"));
    }

    public ObjectToDtoRuleEngine registerRule(ObjectToDto<String, PayloadDto> rule) {
        rules.add(rule);
        return this;
    }
}