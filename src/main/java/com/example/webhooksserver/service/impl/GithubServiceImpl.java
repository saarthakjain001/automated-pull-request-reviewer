package com.example.webhooksserver.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.time.LocalDate;

import com.example.webhooksserver.domain.JiraTicket;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.dtos.PushDetailDto;
import com.example.webhooksserver.dtos.ReviewCommentDto;
import com.example.webhooksserver.gitUtils.enums.GitEvents;
import com.example.webhooksserver.gitUtils.enums.JiraTicketStatus;
import com.example.webhooksserver.gitUtils.enums.ParseSplit;
import com.example.webhooksserver.gitUtils.enums.PullRequestAction;
import com.example.webhooksserver.mapper.EntityToIssueDto;
import com.example.webhooksserver.mapper.IssueDtoToEntity;
import com.example.webhooksserver.repository.GitRepoRepository;
import com.example.webhooksserver.repository.JiraTicketRepository;
import com.example.webhooksserver.repository.TaskToIssueMappingRepository;
import com.example.webhooksserver.ruleEngine.ObjectToDtoRuleEngine;
import com.example.webhooksserver.ruleEngine.ObjectToPullRequestDetailDto;
import com.example.webhooksserver.ruleEngine.ObjectToPushDetailDto;
import com.example.webhooksserver.service.api.GithubService;
import com.example.webhooksserver.service.api.TaskService;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import com.example.webhooksserver.gitUtils.ParserUtils;

@Service
@Slf4j
public class GithubServiceImpl implements GithubService {

    private final ObjectToDtoRuleEngine ruleEngine;
    private final TaskService taskService;

    GithubServiceImpl(ObjectToDtoRuleEngine ruleEngine, TaskService taskService) {
        this.ruleEngine = ruleEngine;
        this.taskService = taskService;
    }

    @Override
    public void gitEventListener(String payload, String event) {
        String repoName = ruleEngine.rule(event, payload).getRepository().getFull_name();
        taskService.executeTasksForRepo(repoName, payload, event);
    }

}
