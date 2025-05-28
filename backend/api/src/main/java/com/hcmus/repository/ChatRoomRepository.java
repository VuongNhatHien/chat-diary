package com.hcmus.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmus.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

	List<ChatRoom> findAllByCreatedAtBetween(Instant startDate, Instant endDate);
}
