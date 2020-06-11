package com.example.webhooksserver.repository;

import com.example.webhooksserver.domain.IssueType;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueTypeRepository extends JpaRepository<IssueType, Long> {
    IssueType findByIssueType(String issueType);
}