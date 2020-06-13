package com.example.webhooksserver.processors.taskProcessorImpl;

import com.example.webhooksserver.processors.TaskProcessor;

import org.springframework.stereotype.Component;

@Component
public class TestCaseProcessor implements TaskProcessor {

    @Override
    public void processTask(String payload, String event, Long taskId) {
        // TODO Auto-generated method stub

    }

}