package com.example.webhooksserver.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.webhooksserver.controller.JiraController;
import com.example.webhooksserver.domain.JiraTicket;
import com.example.webhooksserver.domain.PullRequestDetail;
import com.example.webhooksserver.domain.PushDetail;
import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.PayloadDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.dtos.PushDetailDto;
import com.example.webhooksserver.dtos.ReviewCommentDto;
import com.example.webhooksserver.gitUtils.enums.JiraTicketStatus;
import com.example.webhooksserver.gitUtils.enums.ToDoEnum;
import com.example.webhooksserver.mapper.EntityToIssueDto;
import com.example.webhooksserver.mapper.IssueDtoToEntity;
import com.example.webhooksserver.mapper.PullRequestDetailDtoToEntity;
import com.example.webhooksserver.mapper.PushDetailDtoToEntity;
import com.example.webhooksserver.repository.JiraTicketRepository;
import com.example.webhooksserver.repository.PullRequestDetailRepository;
import com.example.webhooksserver.repository.PushDetailRepository;
import com.example.webhooksserver.service.api.GithubService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.webhooksserver.gitUtils.Pair;
import com.example.webhooksserver.gitUtils.ParserUtils;

@Service
public class GithubServiceImpl implements GithubService {

    private final PushDetailRepository prDetailRepository;
    private final PullRequestDetailRepository pullRepository;
    private final JiraTicketRepository jiraTicketRepository;

    GithubServiceImpl(PushDetailRepository prDetailRepository, PullRequestDetailRepository pullRepository,
            JiraTicketRepository jiraTicketRepository) {
        this.prDetailRepository = prDetailRepository;
        this.pullRepository = pullRepository;
        this.jiraTicketRepository = jiraTicketRepository;
    }

    @Override
    public PushDetail getPushDetails(String jsonObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        PushDetailDtoToEntity dtoToEntity = new PushDetailDtoToEntity();
        try {
            PushDetail prDetail = dtoToEntity.convertToEntity(objectMapper.readValue(jsonObject, PushDetailDto.class));
            prDetailRepository.save(prDetail);
            return prDetail;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public PullRequestDetail getPullRequestDetails(String jsonObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        PullRequestDetailDtoToEntity dtoToEntity = new PullRequestDetailDtoToEntity();
        try {
            System.out.println(objectMapper.readValue(jsonObject, PullRequestDetailDto.class));
            PullRequestDetail prDetail = dtoToEntity
                    .convertToEntity(objectMapper.readValue(jsonObject, PullRequestDetailDto.class));
            pullRepository.save(prDetail);
            return prDetail;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public String getPushChanges(String httpLink) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = httpLink;
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }

    @Override
    public String getPullRequestChanges(String httpLink) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = httpLink;
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }

    @Override
    public List<String> parseToDos(String content) {
        List<String> taskList = new ArrayList<>();

        System.out.println(content.length());
        // Instant start = Instant.now();
        BufferedReader reader = new BufferedReader(new StringReader(content));
        int enumLength = ToDoEnum.TODO.toString().length();
        String todo = ToDoEnum.TODO.toString();
        try {
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {
                currentLine = currentLine.trim();
                Integer startIndex = -1;
                if (currentLine.length() > (3) && currentLine.charAt(0) == '+'
                        && (startIndex = currentLine.indexOf(todo)) != -1) {
                    taskList.add(currentLine.substring(startIndex + enumLength).trim());
                    // taskList.add(ParserUtils.removeDate(currentLine.substring(startIndex +
                    // enumLength)).trim());
                }
            }
            System.out.println(taskList);
            return taskList;
        } catch (IOException e) {
            e.printStackTrace();
            return taskList;
        }

    }

    @Override
    public List<LocalDate> parseDate(List<String> parseStrings) {
        System.out.println(parseStrings);
        List<LocalDate> taskEndDates = new ArrayList<>();
        for (String parseString : parseStrings) {
            taskEndDates.add(ParserUtils.parseDate(parseString));
        }
        return taskEndDates;
        // String regex = "(\\d{2}/\\d{2}/\\d{4})";
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        // Pattern datePattern = Pattern.compile(regex);
        // for (String parseString : parseStrings) {
        // parseString.trim();
        // Matcher m = datePattern.matcher(parseString);
        // if (m.find() == true) {
        // LocalDate date = LocalDate.parse(m.group(0), formatter);
        // taskEndDates.add(date);
        // } else {
        // taskEndDates.add(null);
        // }
        // }

    }

    @Override
    public IssueDto generateJirasFromPush(String payload) {
        PushDetail details = this.getPushDetails(payload);
        String differences = this.getPushChanges(details.getCompare());
        IssueDto tasks = new IssueDto();
        tasks.setTasks(this.parseToDos(differences));
        tasks.setUsername(details.getSender());
        tasks.setDueDates(this.parseDate(this.parseToDos(differences)));
        System.out.println(tasks);
        return tasks;
    }

    @Override
    public HashMap<String, List<Integer>> getLinesTodosWithoutDates(String content) {
        HashMap<String, List<Integer>> separateTodos = new HashMap<>();
        List<Integer> positionNumbers = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new StringReader(content));
        String currentFileName = null;
        String checkFile = null;
        Integer position = -1;
        Integer checkLine = 0;
        try {
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {
                position += 1;
                if ((checkFile = ParserUtils.getfileName(currentLine)) != null) {
                    if (currentFileName != null)
                        separateTodos.put(currentFileName, positionNumbers);
                    positionNumbers = new ArrayList<>();
                    position = -1;
                    currentFileName = checkFile;
                } else if (currentLine.length() > 0 && currentLine.charAt(0) == '+') {
                    if (ParserUtils.isTodo(currentLine) && !ParserUtils.hasDate(currentLine)) {
                        System.out.println(currentLine + " " + position.toString());
                        positionNumbers.add(position);
                    }
                }

            }
            separateTodos.put(currentFileName, positionNumbers);
            return separateTodos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String putComment(HashMap<String, List<Integer>> todosWithoutDates,
            PullRequestDetailDto pullRequestDetailDto) {
        String url = "https://api.github.com";
        url += "/repos/" + pullRequestDetailDto.getRepository().getFull_name() + "/pulls/"
                + pullRequestDetailDto.getNumber().toString() + "/comments";
        System.out.println(url);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("User-Agent", "saarthakjain001");
        headers.add("Authorization", "token 71a98124618207166888449f31265a1c20562179");
        for (Map.Entry<String, List<Integer>> elements : todosWithoutDates.entrySet()) {
            String filename = elements.getKey();
            List<Integer> lineNumbers = elements.getValue();
            for (int i = 0; i < lineNumbers.size(); i++) {
                ReviewCommentDto comment = new ReviewCommentDto(filename, lineNumbers.get(i), "RIGHT",
                        "End Date not mentioned", pullRequestDetailDto.getPull_request().getHead().getSha());
                HttpEntity<ReviewCommentDto> entity = new HttpEntity<>(comment, headers);
                System.out.println(entity);
                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
                if (response.getStatusCode() == HttpStatus.CREATED) {
                    System.out.println("Comment Successful");
                }
            }
        }

        return null;
    }

    @Override
    public IssueDto generateJirasFromMerge(PullRequestDetailDto pullRequestDetailDto) {
        String differences = this.getPushChanges(pullRequestDetailDto.getPull_request().getDiff_url());
        IssueDto tasks = new IssueDto();
        tasks.setTasks(this.parseToDos(differences));
        tasks.setUsername(pullRequestDetailDto.getSender().getLogin());
        tasks.setDueDates(this.parseDate(this.parseToDos(differences)));
        System.out.println(tasks);
        return tasks;
    }

    @Override
    public IssueDto generateJirasFromPush(PushDetailDto pushDetailDto) {
        // PushDetail details = this.getPushDetails(pushDetailDto);
        String differences = this.getPushChanges(pushDetailDto.getCompare());
        IssueDto tasks = new IssueDto();
        tasks.setTasks(this.parseToDos(differences));
        tasks.setUsername(pushDetailDto.getSender().getLogin());
        tasks.setDueDates(this.parseDate(this.parseToDos(differences)));
        System.out.println(tasks);
        return tasks;
    }

    @Override
    public int saveJiraTickets(IssueDto tasks) {
        List<String> taskList = tasks.getTasks();
        List<LocalDate> endDateList = tasks.getDueDates();
        for (int i = 0; i < tasks.getTasks().size(); i++) {
            if (endDateList.get(i) != null) {
                try {
                    jiraTicketRepository.save(IssueDtoToEntity.convertToEntity(taskList.get(i), endDateList.get(i)));
                } catch (Exception e) {
                    System.out.println(e);
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public IssueDto getTicketsFromDb() {
        IssueDto tickets = EntityToIssueDto
                .entityToDto(jiraTicketRepository.findAllByStatus(JiraTicketStatus.UNPROCESSED.toString()));
        System.out.println(tickets);
        return tickets;
    }

    @Override
    public int changeJiraTicketStatus(List<Long> id) {
        List<JiraTicket> ticketList = jiraTicketRepository.findAllById(id);
        try {
            for (JiraTicket tickets : ticketList) {
                tickets.setStatus(JiraTicketStatus.PROCESSED.toString());
                jiraTicketRepository.save(tickets);
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;

    }

}

// 71a98124618207166888449f31265a1c20562179

// curl --user "saarthakjain001" --request POST --data
// '{"path":"Dir/filex","side":"RIGHT","body":"End date not
// specified","position":16,"commit_id":"f0065f314a19c3f880b19abdf5ff064d3c9d3971"}'
// https://api.github.com/repos/saarthakjain001/webHookTest/pulls/5/comments
// https://patch-diff.githubusercontent.com/raw/saarthakjain001/webHookTest/pull/5.diff
// curl --user "saarthakjain001" --request POST --data
// '{"path":"Dir/filex","side":"RIGHT","body":"End date not
// specified","position":16,"commit_id":"f0065f314a19c3f880b19abdf5ff064d3c9d3971"}
// https://api.github.com/repos/saarthakjain001/webHookTest/pulls/5/comments

// public LocalDate parseDate(String parseString) {
// String regex = "(\\d{2}/\\d{2}/\\d{4})";
// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
// Pattern datePattern = Pattern.compile(regex);
// parseString.trim();
// Matcher m = datePattern.matcher(parseString);
// if (m.find() == true) {
// LocalDate date = LocalDate.parse(m.group(0), formatter);
// return date;

// } else {
// return null;
// }
// }

// public boolean hasDate(String parseString) {
// String regex = "(\\d{2}/\\d{2}/\\d{4})";
// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
// Pattern datePattern = Pattern.compile(regex);
// parseString.trim();
// Matcher m = datePattern.matcher(parseString);
// if (m.find() == true) {
// return true;

// } else {
// return false;
// }
// }

// public boolean isTodo(String parseString) {
// parseString = parseString.trim();
// Integer startIndex = -1;
// if (parseString.length() > (3) && parseString.charAt(0) == '+'
// && (startIndex = parseString.indexOf(ToDoEnum.TODO.toString())) != -1) {
// return true;
// }
// return false;
// }

// public Integer LineInfo(String parseString) {
// if (parseString.length() > 2 && parseString.charAt(0) == '@' &&
// parseString.charAt(1) == '@') {
// int startIndex = 4;
// while (parseString.charAt(startIndex) != '+')
// startIndex++;
// int endIndex = startIndex;
// while (parseString.charAt(endIndex) != ',') {
// endIndex++;
// }
// return Integer.valueOf(parseString.substring(startIndex, endIndex));
// }
// return -1;
// }

// public String getfileName(String parseString) {
// if (parseString.length() > (3) && parseString.charAt(0) == '+' &&
// parseString.charAt(1) == '+'
// && parseString.charAt(2) == '+' && parseString.charAt(4) == 'b') {
// return parseString.substring(6);
// }
// return null;
// }