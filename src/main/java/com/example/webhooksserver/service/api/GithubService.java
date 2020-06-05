package com.example.webhooksserver.service.api;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.example.webhooksserver.domain.PayloadEntity;
import com.example.webhooksserver.domain.PullRequestDetail;
import com.example.webhooksserver.domain.PushDetail;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.dtos.PushDetailDto;

import io.swagger.client.model.Issue;

public interface GithubService {
    PushDetail getPushDetails(String jsonObject);

    String getPushChanges(String httpLink);

    String getPullRequestChanges(String httpLink);

    List<String> parseToDos(String content);

    List<LocalDate> parseDate(List<String> parseString);

    IssueDto generateJirasFromPush(String payload);

    IssueDto generateJirasFromMerge(PullRequestDetailDto pullRequestDetailDto);

    IssueDto generateJirasFromPush(PushDetailDto pushDetailDto);

    PullRequestDetail getPullRequestDetails(String jsonObject);

    HashMap<String, List<Integer>> getLinesTodosWithoutDates(String content);

    String putComment(HashMap<String, List<Integer>> todosWithoutDates, PullRequestDetailDto pullRequestDetailDto);

    int saveJiraTickets(IssueDto tasks);

    IssueDto getTicketsFromDb();

    int changeJiraTicketStatus(List<Long> id);
}