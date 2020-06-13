package com.example.webhooksserver.processors;

public interface TaskProcessor {
    public void processTask(String payload, String event, Long taskId);
}