package com.example.webhooksserver.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.dtos.ReviewCommentDto;

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
}