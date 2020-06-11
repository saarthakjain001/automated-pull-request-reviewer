package com.example.webhooksserver.service.api;

import com.example.webhooksserver.dtos.RepoTasksDto;
import com.example.webhooksserver.dtos.TaskIssueMappingDto;

public interface ClientService {
    void addRepo(RepoTasksDto object);

    Long createTask(String task);

    void addTaskToRepo(RepoTasksDto object);

    void deleteTaskFromRepo(RepoTasksDto object);

    Long addNewIssueType(String issue);

    void createIssueMapping(TaskIssueMappingDto object);
}