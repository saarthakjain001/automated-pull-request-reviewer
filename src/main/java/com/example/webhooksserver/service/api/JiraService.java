package com.example.webhooksserver.service.api;

import java.time.LocalDate;
import java.util.List;

import com.atlassian.jira.rest.client.api.IssueRestClient;

public interface JiraService {
    List<String> createIssue(String assignee, List<String> tasks, List<LocalDate> dueDates);

    String createEpic(String assignee, String epicTask);

}
