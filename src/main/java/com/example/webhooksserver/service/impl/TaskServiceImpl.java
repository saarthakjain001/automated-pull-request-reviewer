package com.example.webhooksserver.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.example.webhooksserver.enums.TaskEnum;
import com.example.webhooksserver.exceptions.NoSuchRepositoryException;
import com.example.webhooksserver.exceptions.NotImplementedException;
import com.example.webhooksserver.processors.TaskProcessorFactory;
import com.example.webhooksserver.repository.GitRepoRepository;
import com.example.webhooksserver.repository.GitRepoTaskMappingRepository;
import com.example.webhooksserver.repository.TaskRepository;
import com.example.webhooksserver.service.api.GithubService;
import com.example.webhooksserver.service.api.TaskService;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final GitRepoTaskMappingRepository gitRepoTaskMappingRepository;
    private final GitRepoRepository gitRepoRepository;
    private final TaskProcessorFactory taskProcessorFactory;
    private final TaskRepository taskRepository;
    private final GithubService githubService;

    TaskServiceImpl(GitRepoRepository gitRepoRepository, GitRepoTaskMappingRepository gitRepoTaskMappingRepository,
            TaskProcessorFactory taskProcessorFactory, TaskRepository taskRepository, GithubService githubService) {
        this.gitRepoRepository = gitRepoRepository;
        this.gitRepoTaskMappingRepository = gitRepoTaskMappingRepository;
        this.taskProcessorFactory = taskProcessorFactory;
        this.taskRepository = taskRepository;
        this.githubService = githubService;
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
    public void executeTasksForRepo(String payload, String event) {
        getTasksForRepo(getRepoId(githubService.getRepoName(payload, event)))
                .forEach(taskId -> processTask(taskId, payload, event));
    }

    public void processTask(Long taskId, String payload, String event) {

        switch (TaskEnum.valueOf(taskRepository.findById(taskId).get().getTaskType())) {
            case TODO:
                taskProcessorFactory.getTaskProcessor(TaskEnum.TODO).processTask(payload, event, taskId);
                break;
            case REFACTOR:
                taskProcessorFactory.getTaskProcessor(TaskEnum.REFACTOR).processTask(payload, event, taskId);
                break;
            default:
                throw new NotImplementedException();
        }
    }

}