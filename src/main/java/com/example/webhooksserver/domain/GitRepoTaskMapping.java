package com.example.webhooksserver.domain;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class GitRepoTaskMapping extends BaseEntity {
    private Long repoId;
    private Long taskId;

    GitRepoTaskMapping() {

    }
}