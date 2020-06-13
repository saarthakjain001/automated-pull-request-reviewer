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
import com.example.webhooksserver.domain.ProjectIssueTypes;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.JiraProjectForGitRepoDto;
import com.example.webhooksserver.dtos.JiraProjectIssueTypeDto;
import com.example.webhooksserver.dtos.TodoDto;
import com.example.webhooksserver.gitUtils.ParserUtils;
import com.example.webhooksserver.enums.JiraFields;
import com.example.webhooksserver.repository.GitJiraClientRepository;
import com.example.webhooksserver.repository.GitRepoRepository;
import com.example.webhooksserver.repository.IssueTypeRepository;
import com.example.webhooksserver.repository.JiraEntriesRepository;
import com.example.webhooksserver.repository.ProjectIssueTypesRepository;
import com.example.webhooksserver.repository.TaskToIssueMappingRepository;
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

    private final JiraEntriesRepository jiraEntriesRepository;
    private final GitJiraClientRepository gitJiraClientRepository;
    private final ProjectIssueTypesRepository projectIssueTypesRepository;
    private final IssueTypeRepository issueTypeRepository;
    private final GitRepoRepository gitRepoRepository;
    private final TaskToIssueMappingRepository taskToIssueMappingRepository;

    JiraServiceImpl(GitJiraClientRepository gitJiraClientRepository,
            ProjectIssueTypesRepository projectIssueTypesRepository, IssueTypeRepository issueTypeRepository,
            JiraEntriesRepository jiraEntriesRepository, GitRepoRepository gitRepoRepository,
            TaskToIssueMappingRepository taskToIssueMappingRepository) {

        this.gitJiraClientRepository = gitJiraClientRepository;
        this.projectIssueTypesRepository = projectIssueTypesRepository;
        this.issueTypeRepository = issueTypeRepository;
        this.jiraEntriesRepository = jiraEntriesRepository;
        this.gitRepoRepository = gitRepoRepository;
        this.taskToIssueMappingRepository = taskToIssueMappingRepository;

    }

    @Override
    public void saveJiraTickets(IssueDto tasks, Long taskId) {
        List<String> taskList = tasks.getTasks();
        List<LocalDate> endDateList = tasks.getDueDates();
        String repoName = tasks.getRepoName();
        Long repoId = gitRepoRepository.findByRepoName(repoName).getId();
        Long issueId = taskToIssueMappingRepository.findByTaskId(taskId).getIssueId();
        for (int i = 0; i < tasks.getTasks().size(); i++) {
            if (endDateList.get(i) != null) {
                JiraEntries newJiraEntry = JiraEntries.builder().task(taskList.get(i)).endDate(endDateList.get(i))
                        .repoName(repoName).repoId(repoId).issueId(issueId).build();
                jiraEntriesRepository.save(newJiraEntry);
            }
        }
        return;

    }

    @Override
    public void generateJiras() {
        TodoDto generatedTickets = createIssue(getTicketsFromDb());
        changeJiraTicketStatus(generatedTickets.getId(), generatedTickets.getJiraTicketKey());
    }

    public TodoDto createIssue(List<JiraEntries> tickets) {
        JiraRestClient myJiraClient = new JiraClient(config).getRestClient();
        IssueRestClient issueClient = myJiraClient.getIssueClient();

        List<Long> successfulIds = new ArrayList<>();
        List<String> retrievedJiraKeys = new ArrayList<>();
        for (int i = 0; i < tickets.size(); i++) {

            String projectKey = gitJiraClientRepository.findByRepoName(tickets.get(i).getRepoName()).getProjectKey();
            String issueType = issueTypeRepository.findById(tickets.get(i).getIssueId()).get().getIssueType();
            Long issueId = projectIssueTypesRepository.findByProjectKeyAndIssueType(projectKey, issueType).getIssueId();
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

    public List<JiraEntries> getTicketsFromDb() {
        return jiraEntriesRepository.findAllByProcessed(false);
    }

    public void changeJiraTicketStatus(List<Long> id, List<String> jiraId) {
        List<JiraEntries> ticketList = jiraEntriesRepository.findAllById(id);
        try {
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
    public void createJiraProjectIssueTypes(JiraProjectIssueTypeDto obj) {
        projectIssueTypesRepository.save(ProjectIssueTypes.builder().projectKey(obj.getProjectKey())
                .issueType(obj.getIssueType()).issueId(obj.getIssueId()).build());
    }

    @Override
    public void createProject(JiraProjectForGitRepoDto obj) {
        gitJiraClientRepository
                .save(GitJiraClient.builder().repoName(obj.getRepoName()).projectKey(obj.getProjectKey()).build());

    }

}
