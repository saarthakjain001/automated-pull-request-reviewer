package com.example.webhooksserver.repository;

import java.util.List;

import com.example.webhooksserver.domain.JiraTicket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JiraTicketRepository extends JpaRepository<JiraTicket, Long> {
    List<JiraTicket> findAllByStatus(String status);

    @Modifying
    @Query("update JiraTicket u set u.status = ?1 where u.id = ?2")
    int setStatusFor(String status, Long id);
}