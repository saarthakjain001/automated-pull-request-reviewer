package com.example.webhooksserver.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestDto {
    private String diff_url;
    private HeadCommitDto head;
    private Date merged_at;
}