package com.example.webhooksserver.mapper;

import java.text.ParseException;
import java.time.LocalDate;

import com.example.webhooksserver.domain.JiraTicket;

import lombok.Builder;

public class IssueDtoToEntity {
    public static JiraTicket convertToEntity(String task, LocalDate date, String repoName) {
        JiraTicket ticket = new JiraTicket();
        ticket.setTask(task);
        ticket.setEndDate(date);
        ticket.setRepoName(repoName);
        return ticket;
        // return JiraTicket.builder().task(task).endDate(date).build();
    }
}