
package com.example.webhooksserver.controller;

import java.util.Map;

import com.example.webhooksserver.domain.GitJiraClient;
import com.example.webhooksserver.domain.ProjectIssueTypes;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.JiraProjectForGitRepoDto;
import com.example.webhooksserver.dtos.JiraProjectIssueTypeDto;
import com.example.webhooksserver.dtos.TodoDto;
import com.example.webhooksserver.repository.GitJiraClientRepository;
import com.example.webhooksserver.repository.ProjectIssueTypesRepository;
import com.example.webhooksserver.service.api.JiraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jira")
public class JiraController {

    private final JiraService jiraService;

    public JiraController(JiraService jiraService) {
        this.jiraService = jiraService;

    }

    @PostMapping("/repo-project")
    public void createProject(@RequestBody JiraProjectForGitRepoDto obj) {
        jiraService.createProject(obj);
    }

    @PostMapping("/project-issue-types")
    public void createIssueTypes(@RequestBody JiraProjectIssueTypeDto obj) {
        jiraService.createJiraProjectIssueTypes(obj);
    }
}
