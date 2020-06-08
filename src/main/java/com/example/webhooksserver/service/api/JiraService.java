package com.example.webhooksserver.service.api;

import java.time.LocalDate;
import java.util.List;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.example.webhooksserver.client.JiraClient;

public interface JiraService {
    List<Long> createIssue(String assignee, List<String> tasks, List<LocalDate> dueDates, List<Long> id);

    String createEpic(String assignee, String epicTask, JiraRestClient myJiraClient);

}
