package com.example.webhooksserver.domain;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class TaskToIssueMapping extends BaseEntity {
    private Long taskId;
    private Long issueId;

    TaskToIssueMapping() {

    }
}