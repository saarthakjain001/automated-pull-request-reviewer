package com.example.webhooksserver.service.impl;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.example.webhooksserver.client.JiraClient;
import com.example.webhooksserver.config.JiraConfig;
import com.example.webhooksserver.domain.GitJiraClient;
import com.example.webhooksserver.domain.JiraEntries;
import com.example.webhooksserver.domain.JiraTicket;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.TodoDto;
import com.example.webhooksserver.gitUtils.ParserUtils;
import com.example.webhooksserver.gitUtils.enums.JiraFields;
import com.example.webhooksserver.mapper.EntityToIssueDto;
import com.example.webhooksserver.repository.GitJiraClientRepository;
import com.example.webhooksserver.repository.IssueTypeRepository;
import com.example.webhooksserver.repository.JiraEntriesRepository;
import com.example.webhooksserver.repository.JiraTicketRepository;
import com.example.webhooksserver.repository.ProjectIssueTypesRepository;
import com.example.webhooksserver.service.api.JiraService;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.atlassian.util.concurrent.Promise;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JiraServiceImpl implements JiraService {

    @Autowired
    private JiraConfig config;

    private final JiraTicketRepository jiraTicketRepository;
    private final JiraEntriesRepository jiraEntriesRepository;
    private final GitJiraClientRepository gitJiraClientrepo;
    private final ProjectIssueTypesRepository projectIssueTypesRepo;
    private final IssueTypeRepository issueTypeRepository;
    private Map<String, String> projectKeys;

    JiraServiceImpl(JiraTicketRepository jiraTicketRepository, GitJiraClientRepository gitJiraClientRepository,
            ProjectIssueTypesRepository projectIssueTypesRepository, IssueTypeRepository issueTypeRepository,
            JiraEntriesRepository jiraEntriesRepository) {
        this.jiraTicketRepository = jiraTicketRepository;
        gitJiraClientrepo = gitJiraClientRepository;
        projectIssueTypesRepo = projectIssueTypesRepository;
        this.issueTypeRepository = issueTypeRepository;
        this.jiraEntriesRepository = jiraEntriesRepository;

    }

    public TodoDto createIssue(List<JiraEntries> tickets) {
        JiraRestClient myJiraClient = new JiraClient(config).getRestClient();
        IssueRestClient issueClient = myJiraClient.getIssueClient();

        List<Long> successfulIds = new ArrayList<>();
        List<String> retrievedJiraKeys = new ArrayList<>();
        for (int i = 0; i < tickets.size(); i++) {

            String projectKey = gitJiraClientrepo.findByRepoName(tickets.get(i).getRepoName()).getProjectKey();
            String issueType = issueTypeRepository.findById(tickets.get(i).getIssueId()).get().getIssueType();
            // Long issueId = projectIssueTypesRepo.findByProjectKeyAndIssueType(projectKey,
            // config.getTasktype())
            // .getIssueId();
            Long issueId = projectIssueTypesRepo.findByProjectKeyAndIssueType(projectKey, issueType).getIssueId();
            IssueInputBuilder issueBuilder = new IssueInputBuilder(projectKey, issueId,
                    ParserUtils.removeDate(tickets.get(i).getTask()));

            if (tickets.get(i).getEndDate() != null) {
                setDueDate(tickets.get(i).getEndDate(), issueBuilder);

            } else {
                continue;
            }

            IssueInput newIssue = issueBuilder.build();

            Promise<BasicIssue> bPromise = issueClient.createIssue(newIssue);
            try {
                String issueKey = bPromise.claim().getKey();
                retrievedJiraKeys.add(issueKey);
                successfulIds.add(tickets.get(i).getId());
            } catch (Exception e) {
                log.info(e.getMessage());
                continue;
            }

        }
        try {
            myJiraClient.close();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return new TodoDto(successfulIds, retrievedJiraKeys);
    }

    void setDueDate(LocalDate dueDate, IssueInputBuilder issueBuilder) {
        Map<String, Object> date = new HashMap<String, Object>();
        date.put(JiraFields.DUE_DATE.getField(), dueDate.toString());
        FieldInput dueDateField = new FieldInput(JiraFields.DUE_DATE.getField(), dueDate.toString());
        issueBuilder.setFieldInput(dueDateField);
    }

    void setParent(String parentKey, IssueInputBuilder issueBuilder) {
        Map<String, Object> parent = new HashMap<String, Object>();
        parent.put(JiraFields.KEY.getField(), parentKey);
        FieldInput parentField = new FieldInput(JiraFields.PARENT.getField(), new ComplexIssueInputFieldValue(parent));
        issueBuilder.setFieldInput(parentField);
    }

    // public List<JiraTicket> getTicketsFromDb() {
    // return jiraTicketRepository.findAllByProcessed(false);
    // }

    public List<JiraEntries> getTicketsFromDb() {
        return jiraEntriesRepository.findAllByProcessed(false);
    }

    // @Override
    // public void changeJiraTicketStatus(List<Long> id, List<String> jiraId) {
    // List<JiraTicket> ticketList = jiraTicketRepository.findAllById(id);
    // try {
    // // for (JiraTicket tickets : ticketList) {
    // for (int i = 0; i < id.size(); i++) {
    // ticketList.get(i).setProcessed(true);
    // ticketList.get(i).setJiraId(jiraId.get(i));
    // jiraTicketRepository.save(ticketList.get(i));
    // }
    // } catch (Exception e) {
    // return;
    // }
    // return;

    // }
    @Override
    public void changeJiraTicketStatus(List<Long> id, List<String> jiraId) {
        List<JiraEntries> ticketList = jiraEntriesRepository.findAllById(id);
        try {
            // for (JiraTicket tickets : ticketList) {
            for (int i = 0; i < id.size(); i++) {
                ticketList.get(i).setProcessed(true);
                ticketList.get(i).setJiraId(jiraId.get(i));
                jiraEntriesRepository.save(ticketList.get(i));
            }
        } catch (Exception e) {
            return;
        }
        return;

    }

    @Override
    public void generateJiras() {
        TodoDto generatedTickets = createIssue(getTicketsFromDb());
        changeJiraTicketStatus(generatedTickets.getId(), generatedTickets.getJiraTicketKey());
    }

    // @Override
    // public IssueDto getTicketsFromDb() {
    // IssueDto tickets =
    // EntityToIssueDto.entityToDto(jiraTicketRepository.findAllByProcessed(false));
    // System.out.println(tickets);
    // return tickets;
    // }

    // @Override
    // public TodoDto createIssue(IssueDto issueDto) {
    // JiraRestClient myJiraClient = new JiraClient(config).getRestClient();
    // IssueRestClient issueClient = myJiraClient.getIssueClient();

    // List<Long> successfulIds = new ArrayList<>();
    // List<String> retrievedJiraKeys = new ArrayList<>();
    // List<String> tasks = issueDto.getTasks();
    // List<LocalDate> dueDates = issueDto.getDueDates();
    // List<Long> id = issueDto.getId();
    // for (int i = 0; i < tasks.size(); i++) {

    // IssueInputBuilder issueBuilder = new
    // IssueInputBuilder(config.getProjectkey(),
    // Long.valueOf(config.getTasktype()), ParserUtils.removeDate(tasks.get(i)));

    // if (dueDates.get(i) != null) {
    // setDueDate(dueDates.get(i), issueBuilder);

    // } else {
    // continue;
    // }
    // if (config.getParent() != null)
    // setParent(config.getParent(), issueBuilder);

    // IssueInput newIssue = issueBuilder.build();

    // Promise<BasicIssue> bPromise = issueClient.createIssue(newIssue);
    // try {
    // String issueKey = bPromise.claim().getKey();
    // retrievedJiraKeys.add(issueKey);
    // successfulIds.add(id.get(i));
    // } catch (Exception e) {
    // log.info(e.getMessage());
    // continue;
    // }

    // }
    // return new TodoDto(successfulIds, retrievedJiraKeys);
    // }

}
