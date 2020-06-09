package com.example.webhooksserver.ruleEngine;

import java.util.ArrayList;
import java.util.List;

import com.example.webhooksserver.dtos.PayloadDto;
import com.example.webhooksserver.ruleEngine.ObjectToDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RuleEngine {
    List<ObjectToDto<String, PayloadDto>> rules;

    public RuleEngine() {
        rules = new ArrayList<>();
    }

    public PayloadDto rule(String event, String payload) {
        return rules.stream().filter(rule -> rule.matches(event)).map(rule -> rule.convertToDto(payload)).findFirst()
                .orElseThrow(() -> new RuntimeException("No Matching rule found"));
    }

    public RuleEngine registerRule(ObjectToDto<String, PayloadDto> rule) {
        rules.add(rule);
        return this;
    }
}