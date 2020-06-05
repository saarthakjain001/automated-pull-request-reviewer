package com.example.webhooksserver.mapper;

import java.text.ParseException;

import com.example.webhooksserver.domain.PullRequestDetail;
import com.example.webhooksserver.dtos.PullRequestDetailDto;

public class PullRequestDetailDtoToEntity {
    public PullRequestDetail convertToEntity(PullRequestDetailDto pullRequestDto) throws ParseException {
        // PrDetail prDetail = modelMapper.map(prDetailDto, PrDetail.class);
        PullRequestDetail prDetail = new PullRequestDetail();
        prDetail.setSender(pullRequestDto.getSender().toString());
        prDetail.setRepository(pullRequestDto.getRepository().toString());
        prDetail.setCommit_id(pullRequestDto.getPull_request().getHead().getSha());
        prDetail.setAction(pullRequestDto.getAction());
        prDetail.setNumber(pullRequestDto.getNumber());
        prDetail.setDiff_url(pullRequestDto.getPull_request().getDiff_url());
        prDetail.setOwner(pullRequestDto.getRepository().getOwner().toString());
        return prDetail;
    }
}