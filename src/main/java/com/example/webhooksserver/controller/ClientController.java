package com.example.webhooksserver.controller;

import com.example.webhooksserver.dtos.RepoTasksDto;
import com.example.webhooksserver.dtos.TaskIssueMappingDto;
import com.example.webhooksserver.service.api.ClientService;
import com.example.webhooksserver.service.api.TaskService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    ClientController(ClientService clientService) {
        this.clientService = clientService;

    }

    @PostMapping("/add-repo")
    void addRepo(@RequestBody RepoTasksDto repo) {
        clientService.addRepo(repo);
    }

    @PostMapping("/create-task")
    void createTask(@RequestBody String obj) {
        clientService.createTask(obj);

    }

    @PostMapping("/add-task")
    void addTask(@RequestBody RepoTasksDto repo) {
        clientService.addTaskToRepo(repo);
    }

    @PostMapping("/delete-task")
    void deleteTask(@RequestBody RepoTasksDto repo) {
        clientService.deleteTaskFromRepo(repo);
    }

    @PostMapping("/add-new-issue")
    void addIssue(@RequestBody String issueType) {
        clientService.addNewIssueType(issueType);
    }

    @PostMapping("/task-issue-mapping")
    void taskIssueMapping(@RequestBody TaskIssueMappingDto object) {
        clientService.createIssueMapping(object);
    }

    // @PostMapping("/client/task-service")
    // void testTaskService(@RequestBody String repoName) {
    // taskService.executeTasksForRepo(repoName);
    // }
}