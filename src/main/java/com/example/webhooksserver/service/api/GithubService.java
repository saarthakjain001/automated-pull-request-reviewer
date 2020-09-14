package com.example.webhooksserver.service.api;

import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.dtos.PushDetailDto;

public interface GithubService {
    // void gitEventListener(String payload, String event);

    String getRepoName(String payload, String event);

    IssueDto extractTasks(PullRequestDetailDto pullRequestDetailDto);

    IssueDto extractTasks(PushDetailDto pullRequestDetailDto);

    String getCommittedChanges(String httpLink);
}