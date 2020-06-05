
package com.example.webhooksserver.controller;

import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.service.api.JiraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JiraController {

    @Autowired
    private JiraService service;

    JiraController(JiraService service) {
        this.service = service;

    }

    @PostMapping("/jira-service")
    List<String> createTask(@RequestBody IssueDto jsonBody) {
        System.out.println("Inside Controller");
        System.out.println(jsonBody);
        return service.createIssue(jsonBody.getUsername(), jsonBody.getTasks(), jsonBody.getDueDates());
    }
}
