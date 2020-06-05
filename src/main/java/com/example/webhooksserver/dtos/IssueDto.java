package com.example.webhooksserver.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class IssueDto {
    private String username;
    private List<String> tasks;
    private List<LocalDate> dueDates;

    public String getUsername() {
        return username;
    }

    public List<String> getTasks() {
        return tasks;
    }
}
