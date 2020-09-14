package com.example.webhooksserver.repository;

import com.example.webhooksserver.domain.GitRepo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GitRepoRepository extends JpaRepository<GitRepo, Long> {
    GitRepo findByRepoName(String repoName);
}