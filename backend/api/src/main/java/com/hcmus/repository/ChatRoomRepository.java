package com.hcmus.repository;

import com.hcmus.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

    List<ChatRoom> findAllByCreatedAtBetween(Instant startDate, Instant endDate);
}
