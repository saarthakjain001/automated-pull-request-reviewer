package com.example.webhooksserver.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.example.webhooksserver.client.GitClient;
import com.example.webhooksserver.domain.GitRepoTaskMapping;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.gitUtils.enums.GitEvents;
import com.example.webhooksserver.gitUtils.enums.PullRequestAction;
import com.example.webhooksserver.repository.GitRepoRepository;
import com.example.webhooksserver.repository.GitRepoTaskMappingRepository;
import com.example.webhooksserver.service.api.GithubService;
import com.example.webhooksserver.service.api.TaskService;
import com.example.webhooksserver.service.exceptions.NoSuchRepositoryException;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final GitRepoTaskMappingRepository gitRepoTaskMappingRepository;
    private final GitRepoRepository gitRepoRepository;
    private final GitClient gitClient;

    TaskServiceImpl(GitRepoRepository gitRepoRepository, GitRepoTaskMappingRepository gitRepoTaskMappingRepository,
            GitClient gitClient) {
        this.gitRepoRepository = gitRepoRepository;
        this.gitRepoTaskMappingRepository = gitRepoTaskMappingRepository;
        this.gitClient = gitClient;
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
        getTasksForRepo(getRepoId(repoName)).forEach(taskId -> processTask(taskId.intValue(), payload, event));

    }

    public void processTask(int id, String payload, String event) {
        switch (id) {
            case 1:
                taskForTodos(payload, event, Long.valueOf(id));
                log.info("Task 1 process executed");
                break;
            case 2:
                log.info("Task 2 process executed");
                break;
            case 3:
                log.info("Task 3 process executed");
                break;

        }
    }

    @Override
    public void taskForTodos(String payload, String event, Long taskId) {
        gitClient.executeTodoTasks(payload, event, taskId);
    }

}