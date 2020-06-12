package com.example.webhooksserver.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.example.webhooksserver.gitUtils.enums.JiraTicketStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class JiraTicket {
    @Id
    @GeneratedValue
    private Long id;
    private String task;
    private LocalDate endDate;
    // private String status = JiraTicketStatus.UNPROCESSED.toString();
    private boolean processed = false;
    private String repoName;
    private Long repoId;
    private Long issueId;
    private String jiraId;

    public JiraTicket() {

    }
}