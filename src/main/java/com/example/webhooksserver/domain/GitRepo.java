package com.example.webhooksserver.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class GitRepo extends BaseEntity {
    @Column(unique = true)
    private String repoName;

    public GitRepo() {

    }

}