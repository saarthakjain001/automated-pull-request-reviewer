package com.example.webhooksserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "jira")
@Data
public class JiraConfig {
    private String projectkey;
    private String apitoken;
    private String url;
    private String useremail;
}