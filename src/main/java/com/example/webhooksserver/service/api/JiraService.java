package com.example.webhooksserver.service.api;

import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.JiraProjectForGitRepoDto;
import com.example.webhooksserver.dtos.JiraProjectIssueTypeDto;

public interface JiraService {

    void generateJiras();

    void createJiraProjectIssueTypes(JiraProjectIssueTypeDto obj);

    void createProject(JiraProjectForGitRepoDto obj);

    void saveJiraTickets(IssueDto tasks, Long taskId);

}
