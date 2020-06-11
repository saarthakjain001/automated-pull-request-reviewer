package com.example.webhooksserver.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OwnerDto {
    private String login;

    @Override
    public String toString() {
        return login;
    }
}