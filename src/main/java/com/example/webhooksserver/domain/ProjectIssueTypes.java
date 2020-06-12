package com.example.webhooksserver.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class ProjectIssueTypes {
    @Id
    @GeneratedValue
    private Long id;
    private String projectKey;
    private String issueType;
    private Long issueId;

    public ProjectIssueTypes() {

    }

}