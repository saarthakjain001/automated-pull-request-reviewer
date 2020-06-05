package com.example.webhooksserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.webhooksserver.domain.PullRequestDetail;
import com.example.webhooksserver.domain.PushDetail;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.gitUtils.enums.PullRequestAction;
import com.example.webhooksserver.mapper.ObjectToPullRequestDetailDto;
import com.example.webhooksserver.mapper.ObjectToPushDetailDto;
import com.example.webhooksserver.ruleEngine.RuleEngine;
import com.example.webhooksserver.service.api.GithubService;
import com.example.webhooksserver.service.api.JiraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.swagger.client.api.JiraControllerApi;
import io.swagger.client.model.Issue;

@RestController
public class GithubController {

    @Autowired
    private GithubService gitService;
    @Autowired
    private JiraService jiraService;

    GithubController(GithubService gitService, JiraService jiraService) {
        this.gitService = gitService;
        this.jiraService = jiraService;
    }

    @PostMapping("/")
    List<String> sendJira(@RequestBody String payload, @RequestHeader("x-github-event") String event) {
        RuleEngine ruleEngine = new RuleEngine();
        ruleEngine.registerRule(new ObjectToPullRequestDetailDto()).registerRule(new ObjectToPushDetailDto());
        System.out.println(ruleEngine.rule(event, payload));

        System.out.println(event);
        if (event.equals("push")) {
            // IssueDto tasks = gitService.generateJirasFromPush(payload);
            // JiraController jiraController = new JiraController(jiraService);
            // jiraController.createTask(tasks);
            return null;
        } else if (event.equals("pull_request")) {
            PullRequestDetailDto pullRequestDetailDto = (PullRequestDetailDto) ruleEngine.rule(event, payload);
            if (pullRequestDetailDto.getAction().equals(PullRequestAction.closed.toString())) {
                IssueDto tasks = gitService.generateJirasFromMerge(pullRequestDetailDto);
                JiraController jiraController = new JiraController(jiraService);
                jiraController.createTask(tasks);
                return null;
            } else if (pullRequestDetailDto.getAction().equals(PullRequestAction.opened.toString())) {
                PullRequestDetail details = gitService.getPullRequestDetails(payload);
                System.out.println(details);
                String differences = gitService.getPullRequestChanges(details.getDiff_url());
                gitService.putComment(gitService.getLinesTodosWithoutDates(differences),
                        (PullRequestDetailDto) ruleEngine.rule(event, payload));
                return null;
            } else if (pullRequestDetailDto.getAction().equals(PullRequestAction.synchronize.toString())) {
                PullRequestDetail details = gitService.getPullRequestDetails(payload);
                System.out.println(details);
                String differences = gitService.getPullRequestChanges(details.getDiff_url());
                gitService.putComment(gitService.getLinesTodosWithoutDates(differences),
                        (PullRequestDetailDto) ruleEngine.rule(event, payload));
                return null;
            }
        }
        return null;
    }

    @GetMapping("/")
    List<String> getToDoTasks(@RequestBody String link) {
        String changes = gitService.getPushChanges(link);
        return gitService.parseToDos(changes);

    }
}