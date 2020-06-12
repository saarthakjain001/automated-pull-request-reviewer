package com.example.webhooksserver.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.time.LocalDate;

import com.example.webhooksserver.domain.JiraTicket;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.dtos.PushDetailDto;
import com.example.webhooksserver.dtos.ReviewCommentDto;
import com.example.webhooksserver.gitUtils.enums.GitEvents;
import com.example.webhooksserver.gitUtils.enums.JiraTicketStatus;
import com.example.webhooksserver.gitUtils.enums.ParseSplit;
import com.example.webhooksserver.gitUtils.enums.PullRequestAction;
import com.example.webhooksserver.mapper.EntityToIssueDto;
import com.example.webhooksserver.mapper.IssueDtoToEntity;
import com.example.webhooksserver.repository.GitRepoRepository;
import com.example.webhooksserver.repository.JiraTicketRepository;
import com.example.webhooksserver.repository.TaskToIssueMappingRepository;
import com.example.webhooksserver.ruleEngine.ObjectToDtoRuleEngine;
import com.example.webhooksserver.ruleEngine.ObjectToPullRequestDetailDto;
import com.example.webhooksserver.ruleEngine.ObjectToPushDetailDto;
import com.example.webhooksserver.service.api.GithubService;
import com.example.webhooksserver.service.api.TaskService;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import com.example.webhooksserver.gitUtils.ParserUtils;

@Service
@Slf4j
public class GithubServiceImpl implements GithubService {

    @Value("${github.api.url}")
    private String github_api_url;

    @Value("${github.authorization.token}")
    private String auth_token;

    @Value("${github.user.agent}")
    private String user_agent;

    // private final JiraTicketRepository jiraTicketRepository;
    // private final GitRepoRepository gitRepoRepository;
    // private final TaskToIssueMappingRepository taskToIssueMappingRepository;
    private final ObjectToDtoRuleEngine ruleEngine;
    private final TaskService taskService;

    GithubServiceImpl(ObjectToDtoRuleEngine ruleEngine, TaskService taskService) {

        // this.jiraTicketRepository = jiraTicketRepository;
        // this.gitRepoRepository = gitRepoRepository;
        this.ruleEngine = ruleEngine;
        this.taskService = taskService;
        // this.taskToIssueMappingRepository = taskToIssueMappingRepository;
    }

    @Override
    public void gitEventListener(String payload, String event) {
        String repoName = ruleEngine.rule(event, payload).getRepository().getFull_name();
        taskService.executeTasksForRepo(repoName, payload, event);
        // try {
        // switch (GitEvents.valueOfEvent(event)) {
        // case PULL_REQUEST:
        // PullRequestDetailDto pullRequestDetailDto = (PullRequestDetailDto)
        // ruleEngine.rule(event, payload);
        // String differences;
        // switch (PullRequestAction.valueOfAction(pullRequestDetailDto.getAction())) {
        // case CLOSED:
        // IssueDto tasks = generateTasksFromMerge(pullRequestDetailDto);
        // saveJiraTickets(tasks);
        // break;
        // case OPENED:
        // case SYNCHRONIZE:
        // differences =
        // getCommittedChanges(pullRequestDetailDto.getPull_request().getDiff_url());
        // putComment(getTodoLinesWithoutDates(differences), pullRequestDetailDto);
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
    }
    // public String getCommittedChanges(String httpLink) {
    // RestTemplate restTemplate = new RestTemplate();
    // String uri = httpLink;
    // String result = restTemplate.getForObject(uri, String.class);
    // return result;
    // }

    // public List<String> parseToDos(String content) {
    // List<String> taskList = new ArrayList<>();
    // BufferedReader reader = new BufferedReader(new StringReader(content));
    // int enumLength = ParseSplit.TODO.toString().length();
    // String todo = ParseSplit.TODO.toString();
    // try {
    // String currentLine = null;
    // while ((currentLine = reader.readLine()) != null) {
    // currentLine = currentLine.trim();
    // Integer startIndex = -1;
    // if (currentLine.length() > (3) && currentLine.charAt(0) == '+'
    // && (startIndex = currentLine.indexOf(todo)) != -1) {
    // taskList.add(currentLine.substring(startIndex + enumLength).trim());
    // }
    // }
    // return taskList;
    // } catch (IOException e) {
    // e.printStackTrace();
    // return taskList;
    // }

    // }

    // public List<LocalDate> parseDate(List<String> parseStrings) {
    // List<LocalDate> taskEndDates = new ArrayList<>();
    // for (String parseString : parseStrings) {
    // taskEndDates.add(ParserUtils.parseDate(parseString));
    // }
    // return taskEndDates;

    // }

    // public HashMap<String, List<Integer>> getTodoLinesWithoutDates(String
    // content) {
    // HashMap<String, List<Integer>> separateTodos = new HashMap<>();
    // List<Integer> positionNumbers = new ArrayList<>();
    // BufferedReader reader = new BufferedReader(new StringReader(content));
    // String currentFileName = null;
    // String checkFile = null;
    // Integer position = -1;
    // try {
    // String currentLine = null;
    // while ((currentLine = reader.readLine()) != null) {
    // position += 1;
    // if ((checkFile = ParserUtils.getfileName(currentLine)) != null) {
    // if (currentFileName != null)
    // separateTodos.put(currentFileName, positionNumbers);
    // positionNumbers = new ArrayList<>();
    // position = -1;
    // currentFileName = checkFile;
    // } else if (currentLine.length() > 0 && currentLine.charAt(0) == '+') {
    // if (ParserUtils.isTodo(currentLine) && !ParserUtils.hasDate(currentLine)) {
    // positionNumbers.add(position);
    // }
    // }

    // }
    // separateTodos.put(currentFileName, positionNumbers);
    // return separateTodos;
    // } catch (IOException e) {
    // e.printStackTrace();
    // return null;
    // }

    // }

    // public String putComment(HashMap<String, List<Integer>> todosWithoutDates,
    // PullRequestDetailDto pullRequestDetailDto) {
    // String url = github_api_url;
    // url += "/repos/" + pullRequestDetailDto.getRepository().getFull_name() +
    // "/pulls/"
    // + pullRequestDetailDto.getNumber().toString() + "/comments";
    // RestTemplate restTemplate = new RestTemplate();
    // HttpHeaders headers = new HttpHeaders();
    // headers.setContentType(MediaType.APPLICATION_JSON);
    // headers.add("User-Agent", user_agent);
    // headers.add("Authorization", auth_token);
    // for (Map.Entry<String, List<Integer>> elements :
    // todosWithoutDates.entrySet()) {
    // String filename = elements.getKey();
    // List<Integer> lineNumbers = elements.getValue();
    // for (int i = 0; i < lineNumbers.size(); i++) {
    // ReviewCommentDto comment = new ReviewCommentDto(filename, lineNumbers.get(i),
    // "RIGHT",
    // "Task Completion Date not mentioned",
    // pullRequestDetailDto.getPull_request().getHead().getSha());
    // HttpEntity<ReviewCommentDto> entity = new HttpEntity<>(comment, headers);

    // ResponseEntity<String> response = restTemplate.postForEntity(url, entity,
    // String.class);
    // if (response.getStatusCode() != HttpStatus.CREATED) {
    // log.info("Error");
    // }
    // }
    // }

    // return null;
    // }

    // public IssueDto generateTasksFromMerge(PullRequestDetailDto
    // pullRequestDetailDto) {
    // String differences =
    // this.getCommittedChanges(pullRequestDetailDto.getPull_request().getDiff_url());
    // return this.getIssueDto(pullRequestDetailDto.getSender().getLogin(),
    // differences,
    // pullRequestDetailDto.getRepository().getFull_name());
    // }

    // public IssueDto generateTasksFromPush(PushDetailDto pushDetailDto) {
    // String differences = getCommittedChanges(pushDetailDto.getCompare());
    // return getIssueDto(pushDetailDto.getSender().getLogin(), differences,
    // pushDetailDto.getRepository().getFull_name());
    // }

    // public IssueDto getIssueDto(String username, String differences, String
    // repoName) {
    // IssueDto tasks = new IssueDto();
    // tasks.setTasks(parseToDos(differences));
    // tasks.setUsername(username);
    // tasks.setDueDates(parseDate(parseToDos(differences)));
    // tasks.setRepoName(repoName);
    // return tasks;
    // }

    // public void saveJiraTickets(IssueDto tasks, Long taskId) {
    // List<String> taskList = tasks.getTasks();
    // List<LocalDate> endDateList = tasks.getDueDates();
    // String repoName = tasks.getRepoName();
    // Long repoId = gitRepoRepository.findByRepoName(repoName).getId();
    // Long issueId =
    // taskToIssueMappingRepository.findByTaskId(taskId).getIssueId();
    // for (int i = 0; i < tasks.getTasks().size(); i++) {
    // if (endDateList.get(i) != null) {
    // JiraTicket newJiraTicket =
    // JiraTicket.builder().task(taskList.get(i)).endDate(endDateList.get(i))
    // .repoName(repoName).repoId(repoId).issueId(issueId).build();
    // jiraTicketRepository.save(newJiraTicket);
    // // IssueDtoToEntity.convertToEntity(taskList.get(i), endDateList.get(i),
    // // repoName);
    // // jiraTicketRepository.save(IssueDtoToEntity
    // // .convertToEntity(taskList.get(i), endDateList.get(i),
    // // repoName).setRepoId(repoId));
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
    // IssueDto tasks = generateTasksFromMerge(pullRequestDetailDto);
    // saveJiraTickets(tasks, taskId);
    // break;
    // case OPENED:
    // case SYNCHRONIZE:
    // differences =
    // getCommittedChanges(pullRequestDetailDto.getPull_request().getDiff_url());
    // putComment(getTodoLinesWithoutDates(differences), pullRequestDetailDto);
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
