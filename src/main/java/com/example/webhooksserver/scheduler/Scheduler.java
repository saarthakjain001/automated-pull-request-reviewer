package com.example.webhooksserver.scheduler;

import com.example.webhooksserver.service.api.JiraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    @Autowired
    private JiraService jiraService;

    @Scheduled(fixedDelay = 30000)
    public void scheduledTicketGenerator() {
        jiraService.generateJiras();
    }

}