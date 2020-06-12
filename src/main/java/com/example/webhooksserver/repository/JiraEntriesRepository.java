package com.example.webhooksserver.repository;

import java.util.List;

import com.example.webhooksserver.domain.JiraEntries;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JiraEntriesRepository extends JpaRepository<JiraEntries, Long> {
    List<JiraEntries> findAllByProcessed(Boolean processed);
}