package com.example.webhooksserver.repository;

import com.example.webhooksserver.domain.Task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByTaskType(String taskType);
}