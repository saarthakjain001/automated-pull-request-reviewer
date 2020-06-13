package com.example.webhooksserver.service.api;

public interface TaskService {
    void executeTasksForRepo(String repoName, String payload, String event);

    // void taskForTodos(String payload, String event, Long taskId);
}