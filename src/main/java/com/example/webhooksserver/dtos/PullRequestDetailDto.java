package com.example.webhooksserver.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestDetailDto extends PayloadDto {
    private String action;
    private Long number;
    @JsonProperty("pull_request")
    private PullRequestDto pullRequest;
    private GitRepoDto repository;
    private boolean merged;
}