package com.hcmus.repository;

import com.hcmus.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    List<ChatMessage> findByRoomId(String roomId);

    List<ChatMessage> findAllByRoomId(String roomId);
}
