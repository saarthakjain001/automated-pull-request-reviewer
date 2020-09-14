package com.example.webhooksserver.service.impl;

import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.dtos.PushDetailDto;
import com.example.webhooksserver.gitUtils.ParserUtils;
import com.example.webhooksserver.objectToDtoConvertor.ObjectToDtoConvertor;
import com.example.webhooksserver.service.api.GithubService;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubServiceImpl implements GithubService {

    private final ObjectToDtoConvertor objectToDtoConvertor;

    GithubServiceImpl(ObjectToDtoConvertor objectToDtoConvertor) {
        this.objectToDtoConvertor = objectToDtoConvertor;
    }

    @Override
    public String getCommittedChanges(String httpLink) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = httpLink;
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }

    @Override
    public String getRepoName(String payload, String event) {
        return objectToDtoConvertor.rule(event, payload).getRepository().getFullName();
    }

    @Override
    public IssueDto extractTasks(PullRequestDetailDto pullRequestDetailDto) {
        String differences = getCommittedChanges(pullRequestDetailDto.getPullRequest().getDiffUrl());
        return this.getIssueDto(pullRequestDetailDto.getSender().getLogin(), differences,
                pullRequestDetailDto.getRepository().getFullName());
    }

    @Override
    public IssueDto extractTasks(PushDetailDto pushDetailDto) {
        String differences = getCommittedChanges(pushDetailDto.getCompare());
        return getIssueDto(pushDetailDto.getSender().getLogin(), differences,
                pushDetailDto.getRepository().getFullName());
    }

    public IssueDto getIssueDto(String username, String differences, String repoName) {
        IssueDto tasks = new IssueDto();
        tasks.setTasks(ParserUtils.parseToDos(differences));
        tasks.setUsername(username);
        tasks.setDueDates(ParserUtils.parseDate(ParserUtils.parseToDos(differences)));
        tasks.setRepoName(repoName);
        return tasks;
    }
}
