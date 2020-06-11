package com.example.webhooksserver.domain;

import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class JiraEntries extends BaseEntity {
    private Long repoId;
    private Long issueId;
    private String jiraId;
    private boolean processed = false;
    private String summary;
}