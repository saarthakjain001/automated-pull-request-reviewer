package com.example.webhooksserver.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
@Entity
public class PullRequestDetail extends PayloadEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String action;
    private Long number;
    private String commit_id;
    private String diff_url;
    private String owner;

}