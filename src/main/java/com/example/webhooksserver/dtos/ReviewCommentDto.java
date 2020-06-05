package com.example.webhooksserver.dtos;

import lombok.Data;

@Data
public class ReviewCommentDto {
    private String path;
    private Integer position;
    private String side;
    private String body;
    private String commit_id;

    public ReviewCommentDto(String path, Integer position, String side, String body, String commit_id) {
        this.path = path;
        this.position = position;
        this.side = side;
        this.body = body;
        this.commit_id = commit_id;
    }
}