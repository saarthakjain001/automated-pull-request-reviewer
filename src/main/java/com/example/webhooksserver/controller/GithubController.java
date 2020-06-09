package com.example.webhooksserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.webhooksserver.domain.PullRequestDetail;
import com.example.webhooksserver.domain.PushDetail;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.dtos.PushDetailDto;
import com.example.webhooksserver.dtos.TodoDto;
import com.example.webhooksserver.gitUtils.enums.GitEvents;
import com.example.webhooksserver.gitUtils.enums.PullRequestAction;
import com.example.webhooksserver.ruleEngine.ObjectToPullRequestDetailDto;
import com.example.webhooksserver.ruleEngine.ObjectToPushDetailDto;
import com.example.webhooksserver.ruleEngine.RuleEngine;
import com.example.webhooksserver.service.api.GithubService;
import com.example.webhooksserver.service.api.JiraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Autowired
    private JiraController jiraController;

    @Autowired
    private RuleEngine ruleEngine;

    @Autowired
    private ObjectToPullRequestDetailDto objToPullRequestDto;

    @Autowired
    private ObjectToPushDetailDto objToPushDetailDto;

    // GithubController(GithubService gitService, JiraService jiraService) {
    // this.gitService = gitService;
    // this.jiraService = jiraService;
    // }

    @PostMapping("/")
    int handlePayload(@RequestBody String payload, @RequestHeader("x-github-event") String event) {
        // RuleEngine ruleEngine = new RuleEngine();
        // ruleEngine.registerRule(new ObjectToPullRequestDetailDto()).registerRule(new
        // ObjectToPushDetailDto());

        ruleEngine.registerRule(objToPullRequestDto).registerRule(objToPushDetailDto);
        System.out.println(event);
        switch (event) {
            case "pull_request":
                PullRequestDetailDto pullRequestDetailDto = (PullRequestDetailDto) ruleEngine.rule(event, payload);
                String differences;
                switch (pullRequestDetailDto.getAction()) {
                    case "closed":
                        IssueDto tasks = gitService.generateJirasFromMerge(pullRequestDetailDto);
                        return gitService.saveJiraTickets(tasks);
                    case "opened":
                        differences = gitService
                                .getCommittedChanges(pullRequestDetailDto.getPull_request().getDiff_url());
                        gitService.putComment(gitService.getTodoLinesWithoutDates(differences), pullRequestDetailDto);
                        return 1;
                    case "synchronize":
                        differences = gitService
                                .getCommittedChanges(pullRequestDetailDto.getPull_request().getDiff_url());
                        gitService.putComment(gitService.getTodoLinesWithoutDates(differences), pullRequestDetailDto);
                        return 1;
                }
            case "push":
                return 1;
            default:
                return 1;
        }

        // if (event.equals("push")) {
        // // PushDetailDto pushDetailDto = (PushDetailDto) ruleEngine.rule(event,
        // // payload);
        // // IssueDto tasks = gitService.generateJirasFromPush(pushDetailDto);
        // // JiraController jiraController = new JiraController(jiraService);
        // // jiraController.createTask(tasks);
        // return null;
        // } else if (event.equals("pull_request")) {
        // PullRequestDetailDto pullRequestDetailDto = (PullRequestDetailDto)
        // ruleEngine.rule(event, payload);
        // if
        // (pullRequestDetailDto.getAction().equals(PullRequestAction.closed.toString()))
        // {
        // IssueDto tasks = gitService.generateJirasFromMerge(pullRequestDetailDto);
        // // JiraController jiraController = new JiraController(jiraService);
        // // jiraController.createTask(tasks);
        // if (gitService.saveJiraTickets(tasks) == 1)
        // return "Successful";
        // else
        // return "failed";
        // } else if
        // (pullRequestDetailDto.getAction().equals(PullRequestAction.opened.toString()))
        // {
        // // PullRequestDetail details = gitService.getPullRequestDetails(payload);
        // // System.out.println(details);
        // String differences = gitService
        // .getPullRequestChanges(pullRequestDetailDto.getPull_request().getDiff_url());
        // gitService.putComment(gitService.getLinesTodosWithoutDates(differences),
        // pullRequestDetailDto);
        // return null;

        // } else if
        // (pullRequestDetailDto.getAction().equals(PullRequestAction.synchronize.toString()))
        // {
        // // PullRequestDetail details = gitService.getPullRequestDetails(payload);
        // // System.out.println(details);
        // String differences = gitService
        // .getPullRequestChanges(pullRequestDetailDto.getPull_request().getDiff_url());
        // gitService.putComment(gitService.getLinesTodosWithoutDates(differences),
        // pullRequestDetailDto);
        // return null;
        // }
        // }
        // return null;
    }

    // @GetMapping("/")
    // List<String> getToDoTasks(@RequestBody String link) {
    // String changes = gitService.getPushChanges(link);
    // return gitService.parseToDos(changes);

    // }

}