
package com.example.webhooksserver.controller;

import java.util.Map;

import com.example.webhooksserver.domain.GitJiraClient;
import com.example.webhooksserver.domain.ProjectIssueTypes;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.TodoDto;
import com.example.webhooksserver.repository.GitJiraClientRepository;
import com.example.webhooksserver.repository.ProjectIssueTypesRepository;
import com.example.webhooksserver.service.api.JiraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JiraController {

    private final JiraService service;

    @Autowired
    private GitJiraClientRepository repo;

    @Autowired
    private ProjectIssueTypesRepository issueRepo;

    public JiraController(JiraService service) {
        this.service = service;

    }

    // @PostMapping("/jira-handler")
    // public TodoDto createTask(@RequestBody IssueDto jsonBody) {
    // return service.createIssue(jsonBody);
    // }

    @PostMapping("/repo-project")
    public String createProject(@RequestBody Map<String, String> obj) {
        GitJiraClient client = new GitJiraClient();
        client.setRepoName(obj.get("repoName"));
        client.setProjectKey(obj.get("projectKey"));
        repo.save(client);
        return "Saved";
    }

    @PostMapping("/project-issue-types")
    public String createIssueTypes(@RequestBody Map<String, String> obj) {
        ProjectIssueTypes client = new ProjectIssueTypes();
        client.setProjectKey(obj.get("projectKey"));
        client.setIssueType(obj.get("issueType"));
        client.setIssueId(Long.parseLong(obj.get("issueId")));
        issueRepo.save(client);
        return "Saved";
    }
}
