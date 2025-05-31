package com.hcmus.service;

import com.hcmus.constant.GeneralConstant;
import com.hcmus.dto.request.ChatRequest;
import com.hcmus.dto.request.OpenAIChatRequest;
import com.hcmus.dto.request.OpenAIChatRole;
import com.hcmus.dto.request.OpenAIMessage;
import com.hcmus.dto.response.ChatResponse;
import com.hcmus.dto.response.OpenAIResponse;
import com.hcmus.model.ChatMessage;
import com.hcmus.model.ChatRoom;
import com.hcmus.model.ChatSummary;
import com.hcmus.repository.ChatMessageRepository;
import com.hcmus.repository.ChatRoomRepository;
import com.hcmus.repository.ChatSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private static final String SUMMARY_PROMPT =
            """
                    Dưới đây là những cuộc trò chuyện giữa tôi và 1 người bạn trong ngày hôm nay, hãy viết nhật ký cho tôi về cuộc trò chuyện trong ngày hôm nay. Chỉ viết và không giải thích gì thêm:
                    """;
    private final ChatRoomRepository chatRoomRepository;
    private final OpenAIService openAIService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatSummaryRepository chatSummaryRepository;

    @Transactional
    public ChatRoom createChatRoom() {
        return chatRoomRepository.save(ChatRoom.builder().name("New conversation").build());
    }

    private List<OpenAIMessage> findOpenAIMessagesByRoomId(int roomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByRoomId(roomId);
        return chatMessages.stream().map(chatMessage ->
                OpenAIMessage.builder()
                        .role(
                                chatMessage.belongToChatBot() ? OpenAIChatRole.ASSISTANT : OpenAIChatRole.USER)
                        .content(chatMessage.getText())
                        .build()
        ).toList();
    }

    @Transactional
    public ChatResponse createChatMessage(ChatRequest chatRequest) {
        chatMessageRepository.save(ChatMessage.builder()
                .userId(chatRequest.getUserId())
                .roomId(chatRequest.getRoomId())
                .text(chatRequest.getText())
                .build());

        List<OpenAIMessage> openAIMessages = findOpenAIMessagesByRoomId(chatRequest.getRoomId());

        OpenAIResponse openAIResponse = openAIService.chatWithContext(openAIMessages);

        ChatMessage openAIMessage = ChatMessage.builder()
                .userId(GeneralConstant.CHATBOT_USER_ID)
                .roomId(chatRequest.getRoomId())
                .text(openAIResponse.getText())
                .build();
        chatMessageRepository.save(openAIMessage);

        return ChatResponse.builder().text(openAIResponse.getText()).build();
    }

    private List<ChatRoom> findChatRoomsOfDate(LocalDate date) {
        Instant startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        return chatRoomRepository.findAllByCreatedAtBetween(startOfDay, endOfDay);
    }

    private String buildTranscriptFromChatMessages(List<ChatMessage> chatMessages) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ChatMessage message : chatMessages) {
            if (message.belongToUser()) {
                stringBuilder.append("- Tôi: ");
            } else {
                stringBuilder.append("- Họ: ");
            }
            stringBuilder.append(message.getText()).append("\n");
        }

        log.info("::buildTranscriptFromChatMessages::{}", stringBuilder);

        return stringBuilder.toString();
    }

    private String buildTranscriptFromChatMessagesOfRooms(List<Integer> roomIds) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < roomIds.size(); i++) {
            stringBuilder.append(String.format("Cuộc trò chuyện thứ %d:\n", i + 1));
            List<ChatMessage> messages = chatMessageRepository.findAllByRoomId(roomIds.get(i));
            stringBuilder.append(buildTranscriptFromChatMessages(messages));
        }

        return stringBuilder.toString();
    }

    private String summarizeChatMessagesOfRooms(List<Integer> roomIds) {
        String promptMessage = SUMMARY_PROMPT + buildTranscriptFromChatMessagesOfRooms(roomIds);
        OpenAIResponse response = openAIService.chatWithSingleMessage(
                OpenAIChatRequest.builder().message(promptMessage).build());
        return response.getText();
    }

    @Transactional
    public String summarizeChatMessagesByDate(LocalDate date, String userId) {
        List<ChatRoom> chatRooms = findChatRoomsOfDate(date);
        List<Integer> chatRoomIds = chatRooms.stream().map(ChatRoom::getId).toList();

        String summarized = summarizeChatMessagesOfRooms(chatRoomIds);

        chatSummaryRepository.save(
                ChatSummary.builder().date(date).text(summarized).userId(userId).build());

        return summarized;
    }

}
