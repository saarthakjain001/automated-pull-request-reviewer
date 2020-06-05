package com.example.webhooksserver.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
public class PayloadEntity {
    private String sender;
    private String repository;
}