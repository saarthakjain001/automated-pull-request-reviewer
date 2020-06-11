package com.example.webhooksserver.dtos;

import java.util.List;

import lombok.Data;

@Data
public class RepoTasksDto {
    private String repoName;
    private List<Long> tasks;
}