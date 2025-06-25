package com.hcmus.repository;

import com.hcmus.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    List<ChatRoom> findAllByUserIdAndCreatedAtBetween(String userId, Instant startDate, Instant endDate);

    List<ChatRoom> findByUserIdAndCreatedAtBetweenOrderByCreatedAt(String userId, Instant startDate, Instant endDate);

    List<ChatRoom> findByUserIdOrderByCreatedAtDesc(String userId);
}
