package com.example.webhooksserver.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReviewCommentDto {
    private String path;
    private Integer position;
    private String side;
    private String body;
    @JsonProperty("commit_id")
    private String commitId;

    public ReviewCommentDto(String path, Integer position, String side, String body, String commitId) {
        this.path = path;
        this.position = position;
        this.side = side;
        this.body = body;
        this.commitId = commitId;
    }
}