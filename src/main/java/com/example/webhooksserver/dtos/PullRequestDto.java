package com.example.webhooksserver.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestDto {
    private String diff_url;
    private HeadCommitDto head;
}