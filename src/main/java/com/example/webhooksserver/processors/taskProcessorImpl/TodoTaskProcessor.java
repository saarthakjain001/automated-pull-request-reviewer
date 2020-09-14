package com.example.webhooksserver.processors.taskProcessorImpl;

import com.example.webhooksserver.client.GitClient;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.enums.GitEvents;
import com.example.webhooksserver.enums.PullRequestAction;
import com.example.webhooksserver.exceptions.NotImplementedException;
import com.example.webhooksserver.gitUtils.ParserUtils;
import com.example.webhooksserver.processors.TaskProcessor;
import com.example.webhooksserver.objectToDtoConvertor.ObjectToDtoConvertor;
import com.example.webhooksserver.service.api.GithubService;
import com.example.webhooksserver.service.api.JiraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TodoTaskProcessor implements TaskProcessor {

    @Autowired
    private JiraService jiraService;

    @Autowired
    private GitClient gitClient;

    @Autowired
    private ObjectToDtoConvertor objectToDtoConvertor;

    @Autowired
    private GithubService gitService;

    @Override
    public void processTask(String payload, String event, Long taskId) {
        try {
            switch (GitEvents.valueOfEvent(event)) {
                case PULL_REQUEST:
                    PullRequestDetailDto pullRequestDetailDto = (PullRequestDetailDto) objectToDtoConvertor.rule(event,
                            payload);
                    String differences;
                    switch (PullRequestAction.valueOfAction(pullRequestDetailDto.getAction())) {
                        case CLOSED:
                            if (pullRequestDetailDto.getPullRequest().getMergedAt() != null) {
                                IssueDto tasks = gitService.extractTasks(pullRequestDetailDto);
                                jiraService.saveJiraTickets(tasks, taskId);
                            }
                            break;
                        case OPENED:
                        case SYNCHRONIZE:
                            differences = gitService
                                    .getCommittedChanges(pullRequestDetailDto.getPullRequest().getDiffUrl());
                            gitClient.putComment(ParserUtils.getTodoLinesWithoutDates(differences),
                                    pullRequestDetailDto);
                            break;
                    }
                    break;
                case PUSH:
                default:
                    throw new NotImplementedException("Use case not implemented");
            }

        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}