package com.example.webhooksserver.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

    // @ManyToOne
    // @JoinColumn(name = "repoId", referencedColumnName = "id")
    // private GitRepo repo;

    // @OneToOne
    // @JoinColumn(name = "issueId", referencedColumnName = "id")
    // private IssueType issueType;

}