package com.example.webhooksserver.service.api;

import java.time.LocalDate;
import java.util.List;

import com.example.webhooksserver.domain.JiraEntries;
import com.example.webhooksserver.domain.JiraTicket;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.TodoDto;

public interface JiraService {
    // TodoDto createIssue(IssueDto issueDto);
    TodoDto createIssue(List<JiraEntries> tickets);
    // IssueDto getTicketsFromDb();

    void changeJiraTicketStatus(List<Long> id, List<String> jiraId);

    void generateJiras();
}
