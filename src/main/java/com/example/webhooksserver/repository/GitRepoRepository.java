package com.example.webhooksserver.repository;

import java.util.Optional;

import com.example.webhooksserver.domain.GitRepo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GitRepoRepository extends JpaRepository<GitRepo, Long> {
    Optional<GitRepo> findByRepoName(String repoName);
}