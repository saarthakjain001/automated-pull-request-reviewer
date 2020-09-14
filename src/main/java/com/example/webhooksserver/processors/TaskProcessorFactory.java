package com.example.webhooksserver.processors;

import com.example.webhooksserver.enums.TaskEnum;
import com.example.webhooksserver.exceptions.NotImplementedException;
import com.example.webhooksserver.processors.taskProcessorImpl.RefactorProcessor;
import com.example.webhooksserver.processors.taskProcessorImpl.TodoTaskProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TaskProcessorFactory {
    private final RefactorProcessor refactorProcessor;
    private final TodoTaskProcessor todoTaskProcessor;

    @Autowired
    TaskProcessorFactory(RefactorProcessor refactorProcessor, TodoTaskProcessor todoTaskProcessor) {
        this.refactorProcessor = refactorProcessor;
        this.todoTaskProcessor = todoTaskProcessor;
    }

    public TaskProcessor getTaskProcessor(TaskEnum task) {
        switch (task) {
            case TODO:
                return todoTaskProcessor;
            case REFACTOR:
                return refactorProcessor;
            default:
                throw new NotImplementedException();
        }
    }
}