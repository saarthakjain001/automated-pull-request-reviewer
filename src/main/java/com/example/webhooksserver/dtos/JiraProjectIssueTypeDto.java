package com.example.webhooksserver.dtos;

import lombok.Data;

@Data
public class JiraProjectIssueTypeDto {
    private String projectKey;
    private String issueType;
    private Long issueId;
}