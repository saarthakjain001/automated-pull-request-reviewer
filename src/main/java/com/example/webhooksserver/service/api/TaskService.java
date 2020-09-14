package com.example.webhooksserver.service.api;

public interface TaskService {
    void executeTasksForRepo(String payload, String event);

}