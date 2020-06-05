package com.example.webhooksserver.mapper;

import java.text.ParseException;
import java.time.LocalDate;

import com.example.webhooksserver.domain.JiraTicket;
import com.example.webhooksserver.dtos.IssueDto;

public class IssueDtoToEntity {
    public static JiraTicket convertToEntity(String task, LocalDate date) throws ParseException {
        // PrDetail prDetail = modelMapper.map(prDetailDto, PrDetail.class);
        JiraTicket ticket = new JiraTicket();
        ticket.setTask(task);
        ticket.setEndDate(date);
        return ticket;
    }
}