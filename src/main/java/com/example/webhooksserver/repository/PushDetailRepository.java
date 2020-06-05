package com.example.webhooksserver.repository;

import com.example.webhooksserver.domain.PushDetail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PushDetailRepository extends JpaRepository<PushDetail, Long> {

}