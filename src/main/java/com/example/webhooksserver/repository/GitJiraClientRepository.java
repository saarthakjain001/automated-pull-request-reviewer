package com.example.webhooksserver.repository;

import com.example.webhooksserver.domain.GitJiraClient;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GitJiraClientRepository extends JpaRepository<GitJiraClient, Long> {
    GitJiraClient findByRepoName(String repoName);
}