package com.example.webhooksserver.service.api;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.example.webhooksserver.dtos.IssueDto;
import com.example.webhooksserver.dtos.PullRequestDetailDto;
import com.example.webhooksserver.dtos.PushDetailDto;

public interface GithubService {
    void gitEventListener(String payload, String event);
}