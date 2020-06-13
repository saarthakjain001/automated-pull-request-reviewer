package com.example.webhooksserver.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.example.webhooksserver.enums.TaskEnum;
import com.example.webhooksserver.repository.GitRepoRepository;
import com.example.webhooksserver.repository.GitRepoTaskMappingRepository;
import com.example.webhooksserver.repository.TaskRepository;
import com.example.webhooksserver.service.api.TaskService;
import com.example.webhooksserver.exceptions.NoSuchRepositoryException;
import com.example.webhooksserver.processors.TaskProcessorFactory;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final GitRepoTaskMappingRepository gitRepoTaskMappingRepository;
    private final GitRepoRepository gitRepoRepository;
    private final TaskProcessorFactory taskProcessorFactory;
    private final TaskRepository taskRepository;

    TaskServiceImpl(GitRepoRepository gitRepoRepository, GitRepoTaskMappingRepository gitRepoTaskMappingRepository,
            TaskProcessorFactory taskProcessorFactory, TaskRepository taskRepository) {
        this.gitRepoRepository = gitRepoRepository;
        this.gitRepoTaskMappingRepository = gitRepoTaskMappingRepository;
        this.taskProcessorFactory = taskProcessorFactory;
        this.taskRepository = taskRepository;
    }

    Long getRepoId(String repoName) {
        try {
            if (gitRepoRepository.findByRepoName(repoName) != null)
                return gitRepoRepository.findByRepoName(repoName).getId();
            else
                throw new NoSuchRepositoryException();
        } catch (Exception e) {
            log.info(e.getMessage());
            return -1L;
        }
    }

    List<Long> getTasksForRepo(Long repoId) {
        List<Long> taskIds = new ArrayList<>();
        gitRepoTaskMappingRepository.findAllByRepoId(repoId).forEach(mapping -> taskIds.add(mapping.getTaskId()));
        return taskIds;
    }

    @Override
    public void executeTasksForRepo(String repoName, String payload, String event) {
        getTasksForRepo(getRepoId(repoName)).forEach(taskId -> processTask(taskId, payload, event));

    }

    public void processTask(Long taskId, String payload, String event) {

        switch (TaskEnum.valueOf(taskRepository.findById(taskId).get().getTaskType())) {
            case TODO:
                taskProcessorFactory.getTaskProcessor(TaskEnum.TODO).processTask(payload, event, taskId);
                log.info("Task 1 process executed");
                break;
            case TEST_CASE:
                taskProcessorFactory.getTaskProcessor(TaskEnum.TEST_CASE).processTask(payload, event, taskId);
                log.info("Task 2 process executed");
                break;
        }
    }

}