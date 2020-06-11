package com.example.webhooksserver.repository;

import com.example.webhooksserver.domain.TaskToIssueMapping;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskToIssueMappingRepository extends JpaRepository<TaskToIssueMapping, Long> {
    TaskToIssueMapping findByTaskIdOrIssueId(Long taskId, Long issueId);

    TaskToIssueMapping findByTaskId(Long taskId);

    TaskToIssueMapping findByIssueId(Long issueId);
}