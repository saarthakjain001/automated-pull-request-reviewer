package com.example.webhooksserver.service.impl;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.api.domain.input.LinkIssuesInput;
import com.example.webhooksserver.client.JiraClient;
import com.example.webhooksserver.service.api.JiraService;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicUser;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.User;

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

    // @Value("${jira.projectKey}")
    private String projectKey = "FKPROJ";

    private String HOST_URL = "https://internproject.atlassian.net";
    private String USERNAME_EMAIL = "saarthakjain001@gmail.com";
    private String API_TOKEN = "yAJquBc6dwsn0xUeUp1201E0";
    private Long BUG_ISSUE_TYPE = 10003L;
    private Long TASK_ISSUE_TYPE = 10002L;
    private Long EPIC_ISSUE_TYPE = 10004L;
    private Long STORY_ISSUE_TYPE = 10001L;

    JiraRestClient myJiraClient = new JiraClient(USERNAME_EMAIL, API_TOKEN, HOST_URL).getRestClient();

    @Override
    public List<String> createIssue(String assignee, List<String> tasks, List<LocalDate> dueDates) {

        System.out.println("first");
        // String epicKey = createEpic(assignee, "Internship Tasks with Subtasks");
        IssueRestClient issueClient = myJiraClient.getIssueClient();

        List<String> keys = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Map<String, Object> parent = new HashMap<String, Object>();
            parent.put("key", "FKPROJ-27");
            IssueInputBuilder issueBuilder = new IssueInputBuilder(projectKey, TASK_ISSUE_TYPE, tasks.get(i));
            // issueBuilder.setAssigneeName(assignee);

            FieldInput parentField = new FieldInput("parent", new ComplexIssueInputFieldValue(parent));
            issueBuilder.setFieldInput(parentField);

            if (dueDates.get(i) != null) {
                setDueDate(dueDates.get(i), issueBuilder);
            } else {
                continue;
            }

            IssueInput newIssue = issueBuilder.build();
            System.out.println(newIssue.getFields());

            Promise<BasicIssue> bPromise = issueClient.createIssue(newIssue);
            String issueKey = bPromise.claim().getKey();
            keys.add(issueKey);
        }

        return keys;
    }

    @Override
    public String createEpic(String assignee, String epicTask) {
        IssueRestClient issueClient = myJiraClient.getIssueClient();
        IssueInputBuilder issueBuilder = new IssueInputBuilder(projectKey, EPIC_ISSUE_TYPE, epicTask);
        IssueInput newIssue = issueBuilder.build();
        System.out.println(newIssue.getFields());
        return issueClient.createIssue(newIssue).claim().getKey();
    }

    void setDueDate(LocalDate dueDate, IssueInputBuilder issueBuilder) {
        Map<String, Object> date = new HashMap<String, Object>();
        date.put("duedate", dueDate.toString());
        FieldInput dueDateField = new FieldInput("duedate", dueDate.toString());
        issueBuilder.setFieldInput(dueDateField);
    }

}
