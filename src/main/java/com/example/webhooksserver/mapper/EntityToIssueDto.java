package com.example.webhooksserver.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.webhooksserver.domain.JiraTicket;
import com.example.webhooksserver.dtos.IssueDto;

public class EntityToIssueDto {
    public static IssueDto entityToDto(List<JiraTicket> jiraTickets) {
        IssueDto issue = new IssueDto();
        List<Long> id = new ArrayList<>();
        List<String> tasks = new ArrayList<String>();
        List<LocalDate> dueDates = new ArrayList<>();
        for (int i = 0; i < jiraTickets.size(); i++) {
            id.add(jiraTickets.get(i).getId());
            tasks.add(jiraTickets.get(i).getTask());
            dueDates.add(jiraTickets.get(i).getEndDate());
        }
        issue.setId(id);
        issue.setTasks(tasks);
        issue.setDueDates(dueDates);
        return issue;
    }

}