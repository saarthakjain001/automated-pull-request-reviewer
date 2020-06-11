package com.example.webhooksserver.service.api;

import java.time.LocalDate;
import java.util.List;

import com.example.webhooksserver.domain.JiraTicket;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.TodoDto;

public interface JiraService {
    // TodoDto createIssue(IssueDto issueDto);
    TodoDto createIssue(List<JiraTicket> tickets);
    // IssueDto getTicketsFromDb();

    void changeJiraTicketStatus(List<Long> id);

    void generateJiras();
}
