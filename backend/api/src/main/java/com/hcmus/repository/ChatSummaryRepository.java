package com.hcmus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmus.model.ChatSummary;

public interface ChatSummaryRepository extends JpaRepository<ChatSummary, Integer> {
}
