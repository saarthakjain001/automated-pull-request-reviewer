package com.example.webhooksserver.repository;

import com.example.webhooksserver.domain.JiraEntries;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JiraEntriesRepository extends JpaRepository<JiraEntries, Long> {

}