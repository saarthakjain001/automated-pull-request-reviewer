package com.example.webhooksserver.domain;

import java.time.LocalDate;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class JiraEntries extends BaseEntity {
    private Long repoId;
    private Long issueId;
    private String jiraId;
    private boolean processed = false;
    private String summary;

    private String task;
    private LocalDate endDate;
    private String repoName;

    public JiraEntries() {

    }
}