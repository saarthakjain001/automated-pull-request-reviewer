package com.example.webhooksserver.dtos;

import lombok.Data;

@Data
public class PayloadDto {
    public OwnerDto sender;
    public GitRepoDto repository;
}