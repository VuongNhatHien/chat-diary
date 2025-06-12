package com.hcmus.controller;

import com.hcmus.dto.request.ChatRequest;
import com.hcmus.dto.response.ApiResponse;
import com.hcmus.model.ChatMessage;
import com.hcmus.model.ChatRoom;
import com.hcmus.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat-rooms")
    public ApiResponse<ChatRoom> createChatRoom(Principal principal) {
        return ApiResponse.created(chatService.createChatRoom(principal.getName()));
    }

    @GetMapping("/me/chat-rooms")
    public ApiResponse<List<ChatRoom>> getChatRoomsOfMe(Principal principal) {
        return ApiResponse.ok(chatService.getChatRoomsOfUser(principal.getName()));
    }

    @PostMapping("/chat-rooms/{chatRoomId}/messages")
    public ApiResponse<ChatMessage> createMessageInRoom(@PathVariable String chatRoomId, @RequestBody ChatRequest request, Principal principal) {
        request.setRoomId(chatRoomId);
        request.setUserId(principal.getName());
        return ApiResponse.created(chatService.createChatMessage(request));
    }

    @PostMapping("/chat-rooms/{chatRoomId}/responses")
    public ApiResponse<ChatMessage> createChatBotResponse(@PathVariable String chatRoomId) {
        return ApiResponse.created(chatService.createChatbotResponseOfRoom(chatRoomId));
    }

    @PostMapping("/me/summaries/{date}")
    public ApiResponse<String> summarizeChatMessages(@PathVariable LocalDate date,
                                                     Principal principal) {
        return ApiResponse.created(
                chatService.summarizeChatMessagesOfUserByDate(principal.getName(), date));
    }

    @GetMapping("/me/summaries/{date}")
    public ApiResponse<String> getSummarized(@PathVariable LocalDate date, Principal principal) {
        return ApiResponse.ok(chatService.getSummarized(principal.getName(), date));
    }

    @GetMapping("/chat-rooms/{chatRoomId}/messages")
    public ApiResponse<List<ChatMessage>> getChatMessagesOfRoom(@PathVariable String chatRoomId) {
        return ApiResponse.ok(chatService.findChatMessagesOfRoom(chatRoomId));
    }
}
