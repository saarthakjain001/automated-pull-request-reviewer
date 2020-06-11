package com.example.webhooksserver.repository;

import java.util.List;

import com.example.webhooksserver.domain.GitRepoTaskMapping;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GitRepoTaskMappingRepository extends JpaRepository<GitRepoTaskMapping, Long> {
    GitRepoTaskMapping findByRepoIdAndTaskId(Long repoId, Long taskid);

    List<GitRepoTaskMapping> findAllByRepoId(Long repoId);

    List<GitRepoTaskMapping> findAllByTaskId(Long taskId);

    GitRepoTaskMapping findByTaskId(Long taskId);
}