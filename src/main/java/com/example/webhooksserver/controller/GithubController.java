package com.example.webhooksserver.controller;

import com.example.webhooksserver.service.api.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/git-handler")
    void gitEventListener(@RequestBody String payload, @RequestHeader("x-github-event") String event) {
        taskService.executeTasksForRepo(payload, event);
    }
}