package com.example.webhooksserver.mapper;

import java.text.ParseException;
import java.time.LocalDate;

import com.example.webhooksserver.domain.JiraTicket;

import lombok.Builder;

public class IssueDtoToEntity {
    public static JiraTicket convertToEntity(String task, LocalDate date, String repoName) {
        return JiraTicket.builder().task(task).endDate(date).repoName(repoName).build();
    }
}