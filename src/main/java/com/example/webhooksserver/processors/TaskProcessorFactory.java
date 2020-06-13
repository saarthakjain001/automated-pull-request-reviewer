package com.example.webhooksserver.processors;

import com.example.webhooksserver.enums.TaskEnum;
import com.example.webhooksserver.exceptions.NotImplementedException;
import com.example.webhooksserver.processors.taskProcessorImpl.TestCaseProcessor;
import com.example.webhooksserver.processors.taskProcessorImpl.TodoTaskProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TaskProcessorFactory {
    private final TestCaseProcessor testCaseProcessor;
    private final TodoTaskProcessor todoTaskProcessor;

    @Autowired
    TaskProcessorFactory(TestCaseProcessor testCaseProcessor, TodoTaskProcessor todoTaskProcessor) {
        this.testCaseProcessor = testCaseProcessor;
        this.todoTaskProcessor = todoTaskProcessor;
    }

    public TaskProcessor getTaskProcessor(TaskEnum task) {
        switch (task) {
            case TODO:
                log.info("inside get task processor for todo");
                return todoTaskProcessor;
            case TEST_CASE:
                return testCaseProcessor;
            default:
                throw new NotImplementedException();
        }
    }
}