package com.example.webhooksserver.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.example.webhooksserver.gitUtils.enums.JiraTicketStatus;

import lombok.Data;

@Data
@Entity
public class JiraTicket {
    @Id
    @GeneratedValue
    private Long id;
    private String task;
    private LocalDate endDate;
    private String status = JiraTicketStatus.UNPROCESSED.toString();

}