package com.example.webhooksserver.client;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.example.webhooksserver.config.JiraConfig;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;

import java.net.URI;

@Data
public class JiraClient {
    // @Autowired
    // private JiraConfig jiraConfig;
    private String username;
    private String password;
    private String jiraUrl;
    private JiraRestClient restClient;

    // public JiraClient(String username, String password, String jiraUrl) {
    // this.username = username;
    // this.password = password;
    // this.jiraUrl = jiraUrl;
    // this.restClient = getJiraRestClient();
    // }
    public JiraClient(JiraConfig jiraConfig) {
        this.username = jiraConfig.getUseremail();
        this.password = jiraConfig.getApitoken();
        this.jiraUrl = jiraConfig.getUrl();
        this.restClient = getJiraRestClient();
    }

    private JiraRestClient getJiraRestClient() {
        return new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(getJiraUri(), this.username,
                this.password);
    }

    private URI getJiraUri() {
        return URI.create(this.jiraUrl);
    }

    public JiraRestClient getRestClient() {
        return this.restClient;
    }
}