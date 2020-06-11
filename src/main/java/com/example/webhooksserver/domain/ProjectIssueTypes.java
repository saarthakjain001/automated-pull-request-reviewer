package com.example.webhooksserver.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ProjectIssueTypes {
    @Id
    @GeneratedValue
    private Long id;
    private String projectKey;
    private String issueType;
    private Long issueId;

}