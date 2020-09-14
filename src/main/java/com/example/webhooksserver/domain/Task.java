package com.example.webhooksserver.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Table(name = "TASK")
@Builder
@AllArgsConstructor
public class Task extends BaseEntity {

    private String taskType;

    public Task() {

    }

}