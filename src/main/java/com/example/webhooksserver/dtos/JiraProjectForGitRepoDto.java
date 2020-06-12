package com.example.webhooksserver.dtos;

import lombok.Data;

@Data
public class JiraProjectForGitRepoDto {
    private String repoName;
    private String projectKey;
}