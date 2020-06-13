package com.example.webhooksserver.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitRepoDto {
    @JsonProperty("full_name")
    private String fullName;
    private OwnerDto owner;

    @Override
    public String toString() {
        return fullName;
    }
}