package com.hcmus.repository;

import com.hcmus.model.ChatSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ChatSummaryRepository extends JpaRepository<ChatSummary, Integer> {

    List<ChatSummary> findByUserIdAndDate(String userId, LocalDate date);
}
