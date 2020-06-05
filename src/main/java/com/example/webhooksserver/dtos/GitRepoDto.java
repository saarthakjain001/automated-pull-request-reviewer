package com.example.webhooksserver.dtos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitRepoDto {
    private String full_name;
    private OwnerDto owner;

    @Override
    public String toString() {
        return full_name;
    }
}