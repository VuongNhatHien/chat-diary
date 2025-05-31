package com.hcmus.controller;

import com.hcmus.dto.request.ChatRequest;
import com.hcmus.dto.response.ApiResponse;
import com.hcmus.dto.response.ChatResponse;
import com.hcmus.model.ChatRoom;
import com.hcmus.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat-rooms")
    public ApiResponse<ChatRoom> createChatRoom() {
        return ApiResponse.created(chatService.createChatRoom());
    }

    @PostMapping("/chat-messages")
    public ApiResponse<ChatResponse> chat(@RequestBody @Valid ChatRequest chatRequest,
                                          Principal principal) {
        chatRequest.setUserId(principal.getName());
        return ApiResponse.created(chatService.createChatMessage(chatRequest));
    }

    @PostMapping("/summary")
    public ApiResponse<String> summarizeChatMessages(@RequestParam LocalDate date,
                                                     Principal principal) {
        return ApiResponse.created(
                chatService.summarizeChatMessagesByDate(date, principal.getName()));
    }
}
