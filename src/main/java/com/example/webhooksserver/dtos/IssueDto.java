package com.example.webhooksserver.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class IssueDto {
    private List<Long> id;
    private String username;
    private List<String> tasks;
    private List<LocalDate> dueDates;
    private String repoName;

    public String getUsername() {
        return username;
    }

    public List<String> getTasks() {
        return tasks;
    }
}
