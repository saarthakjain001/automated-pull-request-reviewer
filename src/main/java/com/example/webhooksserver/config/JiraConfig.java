package com.example.webhooksserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "jira")
@Data
public class JiraConfig {
    private String projectkey;
    private String apitoken;
    private String url;
    private String useremail;
    private String tasktype;
    private String taskid;
    private String parent = null;
}