package com.example.webhooksserver.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestDto {
    @JsonProperty("diff_url")
    private String diffUrl;
    private HeadCommitDto head;
    @JsonProperty("merged_at")
    private Date mergedAt;
}