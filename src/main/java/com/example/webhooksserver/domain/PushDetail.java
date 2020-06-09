package com.example.webhooksserver.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.example.webhooksserver.dtos.GitRepoDto;
import com.example.webhooksserver.dtos.OwnerDto;

import lombok.Data;

@Data
@Entity
public class PushDetail extends PayloadEntity {
    @Id
    @GeneratedValue
    private Long prId;
    private String compare;
}