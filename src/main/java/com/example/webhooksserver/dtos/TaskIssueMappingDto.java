package com.example.webhooksserver.dtos;

import lombok.Data;

@Data
public class TaskIssueMappingDto {
    private String taskType;
    private String issueType;

}