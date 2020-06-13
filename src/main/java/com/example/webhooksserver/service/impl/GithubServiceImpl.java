package com.example.webhooksserver.service.impl;

import com.example.webhooksserver.ruleEngine.ObjectToDtoRuleEngine;
import com.example.webhooksserver.service.api.GithubService;
import com.example.webhooksserver.service.api.TaskService;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubServiceImpl implements GithubService {

    private final ObjectToDtoRuleEngine ruleEngine;
    private final TaskService taskService;

    GithubServiceImpl(ObjectToDtoRuleEngine ruleEngine, TaskService taskService) {
        this.ruleEngine = ruleEngine;
        this.taskService = taskService;
    }

    @Override
    public void gitEventListener(String payload, String event) {
        String repoName = ruleEngine.rule(event, payload).getRepository().getFullName();
        taskService.executeTasksForRepo(repoName, payload, event);
    }

    public String getCommittedChanges(String httpLink) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = httpLink;
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }
}
