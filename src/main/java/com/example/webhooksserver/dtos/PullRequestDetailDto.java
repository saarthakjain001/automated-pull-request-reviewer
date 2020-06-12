package com.example.webhooksserver.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestDetailDto extends PayloadDto {
    private String action;
    private Long number;
    private PullRequestDto pull_request;
    private GitRepoDto repository;
    private boolean merged;
}