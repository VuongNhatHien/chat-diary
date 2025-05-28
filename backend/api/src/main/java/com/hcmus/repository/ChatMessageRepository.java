package com.hcmus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmus.model.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
	List<ChatMessage> findByRoomId(int roomId);

	List<ChatMessage> findAllByRoomId(Integer roomId);
}
