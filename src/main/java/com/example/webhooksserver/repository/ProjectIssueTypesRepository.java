package com.example.webhooksserver.repository;

import java.util.List;

import com.example.webhooksserver.domain.ProjectIssueTypes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectIssueTypesRepository extends JpaRepository<ProjectIssueTypes, Long> {
    List<ProjectIssueTypes> findAllByProjectKey(String projectKey);

    ProjectIssueTypes findByProjectKeyAndIssueType(String projectKey, String IssueType);
}