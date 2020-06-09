package com.example.webhooksserver.service.impl;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.api.domain.input.LinkIssuesInput;
import com.example.webhooksserver.client.JiraClient;
import com.example.webhooksserver.config.JiraConfig;
import com.example.webhooksserver.dtos.TodoDto;
import com.example.webhooksserver.gitUtils.ParserUtils;
import com.example.webhooksserver.service.api.JiraService;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicUser;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//import com.atlassian.jira.rest.client.domain.User;
import com.atlassian.util.concurrent.Promise;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JiraServiceImpl implements JiraService {

    @Autowired
    private JiraConfig config;

    // private Long BUG_ISSUE_TYPE = 10003L;
    // private Long TASK_ISSUE_TYPE = 10002L;
    // private Long EPIC_ISSUE_TYPE = 10004L;
    // private Long STORY_ISSUE_TYPE = 10001L;

    @Override
    public TodoDto createIssue(String assignee, List<String> tasks, List<LocalDate> dueDates, List<Long> id) {
        JiraRestClient myJiraClient = new JiraClient(config).getRestClient();
        IssueRestClient issueClient = myJiraClient.getIssueClient();

        List<Long> successfulIds = new ArrayList<>();
        List<String> retrievedJiraKeys = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {

            IssueInputBuilder issueBuilder = new IssueInputBuilder(config.getProjectkey(),
                    Long.valueOf(config.getTasktype()), ParserUtils.removeDate(tasks.get(i)));

            if (dueDates.get(i) != null) {
                setDueDate(dueDates.get(i), issueBuilder);

            } else {
                continue;
            }
            if (config.getParent() != null)
                setParent(config.getParent(), issueBuilder);

            IssueInput newIssue = issueBuilder.build();
            System.out.println(newIssue.getFields());

            Promise<BasicIssue> bPromise = issueClient.createIssue(newIssue);
            try {
                String issueKey = bPromise.claim().getKey();
                retrievedJiraKeys.add(issueKey);
                successfulIds.add(id.get(i));
            } catch (Exception e) {
                System.out.println(e);
                continue;
            }

        }
        return new TodoDto(successfulIds, retrievedJiraKeys);
    }

    // @Override
    // public String createEpic(String assignee, String epicTask, JiraRestClient
    // myJiraClient) {
    // IssueRestClient issueClient = myJiraClient.getIssueClient();
    // IssueInputBuilder issueBuilder = new
    // IssueInputBuilder(config.getProjectkey(), EPIC_ISSUE_TYPE, epicTask);
    // IssueInput newIssue = issueBuilder.build();
    // System.out.println(newIssue.getFields());
    // return issueClient.createIssue(newIssue).claim().getKey();
    // }

    void setDueDate(LocalDate dueDate, IssueInputBuilder issueBuilder) {
        Map<String, Object> date = new HashMap<String, Object>();
        date.put("duedate", dueDate.toString());
        FieldInput dueDateField = new FieldInput("duedate", dueDate.toString());
        issueBuilder.setFieldInput(dueDateField);
    }

    void setParent(String parentKey, IssueInputBuilder issueBuilder) {
        Map<String, Object> parent = new HashMap<String, Object>();
        parent.put("key", parentKey);
        FieldInput parentField = new FieldInput("parent", new ComplexIssueInputFieldValue(parent));
        issueBuilder.setFieldInput(parentField);
    }
}
// System.out.println(config.getProjectkey());
// // System.out.println(config.getUrl());
// // System.out.println(config.getUseremail());
// System.out.println(api_token);
// // System.out.println("first");

// JiraRestClient myJiraClient = new JiraClient(config.getUseremail(),
// config.getApitoken(), config.getUrl())
// .getRestClient();

// issueBuilder.setAssigneeName(assignee);

// FieldInput parentField = new FieldInput("parent", new
// ComplexIssueInputFieldValue(parent));
// issueBuilder.setFieldInput(parentField);

// Map<String, Object> parent = new HashMap<String, Object>();
// parent.put("key", "FKPROJ-27");