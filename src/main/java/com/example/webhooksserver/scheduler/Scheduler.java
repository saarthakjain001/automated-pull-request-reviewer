package com.example.webhooksserver.scheduler;

import com.example.webhooksserver.controller.JiraController;
import com.example.webhooksserver.dtos.TodoDto;
import com.example.webhooksserver.service.api.GithubService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    @Autowired
    private GithubService gitService;

    @Autowired
    private JiraController jiraController;

    @Scheduled(fixedDelay = 60000)
    public void scheduledTicketGenerator() {
        TodoDto generatedTickets = jiraController.createTask(gitService.getTicketsFromDb());
        if (gitService.changeJiraTicketStatus(generatedTickets.getId()) == 1) {
            System.out.println("Updation Successful");
        } else {
            System.out.println("Error in updating table");
        }
    }

}