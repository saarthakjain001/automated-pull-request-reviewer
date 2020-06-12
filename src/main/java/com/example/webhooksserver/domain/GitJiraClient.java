package com.example.webhooksserver.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@AllArgsConstructor
public class GitJiraClient {
    @Id
    @GeneratedValue
    private Long id;
    private String repoName;
    private String projectKey;

    public GitJiraClient() {

    }

}