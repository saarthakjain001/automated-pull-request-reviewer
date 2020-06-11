package com.example.webhooksserver.controller;

import com.example.webhooksserver.dtos.RepoTasksDto;
import com.example.webhooksserver.dtos.TaskIssueMappingDto;
import com.example.webhooksserver.service.api.ClientService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    private final ClientService clientService;

    ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/client/add-repo")
    void addRepo(@RequestBody RepoTasksDto repo) {
        clientService.addRepo(repo);
    }

    @PostMapping("/client/create-task")
    void createTask(@RequestBody String obj) {
        clientService.createTask(obj);

    }

    @PostMapping("/client/add-task")
    void addTask(@RequestBody RepoTasksDto repo) {
        clientService.addTaskToRepo(repo);
    }

    @PostMapping("/client/delete-task")
    void deleteTask(@RequestBody RepoTasksDto repo) {
        clientService.deleteTaskFromRepo(repo);
    }

    @PostMapping("/client/add-new-issue")
    void addIssue(@RequestBody String issueType) {
        clientService.addNewIssueType(issueType);
    }

    @PostMapping("/client/task-issue-mapping")
    void taskIssueMapping(@RequestBody TaskIssueMappingDto object) {
        clientService.createIssueMapping(object);
    }
}