package com.hcmus.repository;

import com.hcmus.model.ChatSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSummaryRepository extends JpaRepository<ChatSummary, Integer> {
}
