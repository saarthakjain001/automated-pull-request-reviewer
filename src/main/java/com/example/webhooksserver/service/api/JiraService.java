package com.example.webhooksserver.service.api;

import java.time.LocalDate;
import java.util.List;

import com.example.webhooksserver.domain.JiraEntries;
import com.example.webhooksserver.domain.JiraTicket;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.JiraProjectForGitRepoDto;
import com.example.webhooksserver.dtos.JiraProjectIssueTypeDto;
import com.example.webhooksserver.dtos.TodoDto;

public interface JiraService {

    void generateJiras();

    void createJiraProjectIssueTypes(JiraProjectIssueTypeDto obj);

    void createProject(JiraProjectForGitRepoDto obj);
}
