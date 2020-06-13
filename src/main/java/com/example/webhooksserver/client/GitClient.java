package com.example.webhooksserver.client;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.webhooksserver.domain.JiraEntries;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.dtos.PushDetailDto;
import com.example.webhooksserver.dtos.ReviewCommentDto;
import com.example.webhooksserver.gitUtils.ParserUtils;
import com.example.webhooksserver.enums.GitEvents;
import com.example.webhooksserver.enums.PullRequestAction;
import com.example.webhooksserver.repository.GitRepoRepository;
import com.example.webhooksserver.repository.JiraEntriesRepository;
import com.example.webhooksserver.repository.TaskToIssueMappingRepository;
import com.example.webhooksserver.ruleEngine.ObjectToDtoRuleEngine;
import com.example.webhooksserver.exceptions.NotImplementedException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GitClient {
    @Value("${github.api.url}")
    private String github_api_url;

    @Value("${github.authorization.token}")
    private String auth_token;

    @Value("${github.user.agent}")
    private String user_agent;
    private final JiraEntriesRepository jiraEntriesRepository;
    private final GitRepoRepository gitRepoRepository;
    private final TaskToIssueMappingRepository taskToIssueMappingRepository;
    private final ObjectToDtoRuleEngine ruleEngine;

    GitClient(ObjectToDtoRuleEngine ruleEngine, GitRepoRepository gitRepoRepository,
            TaskToIssueMappingRepository taskToIssueMappingRepository, JiraEntriesRepository jiraEntriesRepository) {

        this.jiraEntriesRepository = jiraEntriesRepository;
        this.gitRepoRepository = gitRepoRepository;
        this.ruleEngine = ruleEngine;
        this.taskToIssueMappingRepository = taskToIssueMappingRepository;
    }

    public String putComment(HashMap<String, List<Integer>> todosWithoutDates,
            PullRequestDetailDto pullRequestDetailDto) {
        String url = github_api_url;
        url += "/repos/" + pullRequestDetailDto.getRepository().getFullName() + "/pulls/"
                + pullRequestDetailDto.getNumber().toString() + "/comments";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("User-Agent", user_agent);
        headers.add("Authorization", auth_token);
        for (Map.Entry<String, List<Integer>> elements : todosWithoutDates.entrySet()) {
            String filename = elements.getKey();
            List<Integer> lineNumbers = elements.getValue();
            for (int i = 0; i < lineNumbers.size(); i++) {
                ReviewCommentDto comment = new ReviewCommentDto(filename, lineNumbers.get(i), "RIGHT",
                        "Task Completion Date not mentioned", pullRequestDetailDto.getPullRequest().getHead().getSha());
                HttpEntity<ReviewCommentDto> entity = new HttpEntity<>(comment, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
                if (response.getStatusCode() != HttpStatus.CREATED) {
                    log.info("Error");
                }
            }
        }

        return null;
    }

    public IssueDto extractTasksFromMerge(PullRequestDetailDto pullRequestDetailDto) {
        String differences = ParserUtils.getCommittedChanges(pullRequestDetailDto.getPullRequest().getDiffUrl());
        return this.getIssueDto(pullRequestDetailDto.getSender().getLogin(), differences,
                pullRequestDetailDto.getRepository().getFullName());
    }

    public IssueDto extractTasksFromPush(PushDetailDto pushDetailDto) {
        String differences = ParserUtils.getCommittedChanges(pushDetailDto.getCompare());
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

    // public void saveJiraTickets(IssueDto tasks, Long taskId) {
    // List<String> taskList = tasks.getTasks();
    // List<LocalDate> endDateList = tasks.getDueDates();
    // String repoName = tasks.getRepoName();
    // Long repoId = gitRepoRepository.findByRepoName(repoName).getId();
    // Long issueId =
    // taskToIssueMappingRepository.findByTaskId(taskId).getIssueId();
    // for (int i = 0; i < tasks.getTasks().size(); i++) {
    // if (endDateList.get(i) != null) {
    // JiraEntries newJiraEntry =
    // JiraEntries.builder().task(taskList.get(i)).endDate(endDateList.get(i))
    // .repoName(repoName).repoId(repoId).issueId(issueId).build();
    // jiraEntriesRepository.save(newJiraEntry);
    // }
    // }
    // return;
    // }

    // public void executeTodoTasks(String payload, String event, Long taskId) {
    // try {
    // switch (GitEvents.valueOfEvent(event)) {
    // case PULL_REQUEST:
    // PullRequestDetailDto pullRequestDetailDto = (PullRequestDetailDto)
    // ruleEngine.rule(event, payload);
    // String differences;
    // switch (PullRequestAction.valueOfAction(pullRequestDetailDto.getAction())) {
    // case CLOSED:
    // if (pullRequestDetailDto.getPullRequest().getMergedAt() != null) {
    // IssueDto tasks = generateTasksFromMerge(pullRequestDetailDto);
    // saveJiraTickets(tasks, taskId);
    // }
    // break;
    // case OPENED:
    // case SYNCHRONIZE:
    // differences = ParserUtils
    // .getCommittedChanges(pullRequestDetailDto.getPullRequest().getDiffUrl());
    // putComment(ParserUtils.getTodoLinesWithoutDates(differences),
    // pullRequestDetailDto);
    // break;
    // }
    // break;
    // case PUSH:
    // default:
    // throw new NotImplementedException("Use case not implemented");
    // }

    // } catch (Exception e) {
    // log.info(e.getMessage());
    // }
    // }

}