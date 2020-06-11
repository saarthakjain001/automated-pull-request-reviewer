package com.example.webhooksserver.controller;

import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.gitUtils.enums.GitEvents;
import com.example.webhooksserver.gitUtils.enums.PullRequestAction;
import com.example.webhooksserver.ruleEngine.ObjectToDtoRuleEngine;
import com.example.webhooksserver.ruleEngine.ObjectToPullRequestDetailDto;
import com.example.webhooksserver.ruleEngine.ObjectToPushDetailDto;
import com.example.webhooksserver.service.api.GithubService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubController {

    @Autowired
    private GithubService gitService;

    // private final ObjectToDtoRuleEngine ruleEngine;

    // GithubController(ObjectToDtoRuleEngine ruleEngine) {
    // this.ruleEngine = ruleEngine;
    // }

    @PostMapping("/git-handler")
    void gitEventListener(@RequestBody String payload, @RequestHeader("x-github-event") String event) {
        gitService.gitEventListener(payload, event);
    }
}