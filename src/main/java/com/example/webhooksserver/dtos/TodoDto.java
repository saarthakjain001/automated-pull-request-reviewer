package com.example.webhooksserver.dtos;

import java.util.List;

import lombok.Data;

@Data
public class TodoDto {

    List<Long> id;
    List<String> jiraTicketKey;

    public TodoDto(List<Long> id, List<String> jiraTicketKey) {
        this.id = id;
        this.jiraTicketKey = jiraTicketKey;
    }
}