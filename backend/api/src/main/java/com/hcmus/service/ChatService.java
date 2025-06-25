package com.hcmus.service;

import com.hcmus.constant.GeneralConstant;
import com.hcmus.dto.request.ChatRequest;
import com.hcmus.dto.request.OpenAIChatRequest;
import com.hcmus.dto.request.OpenAIChatRole;
import com.hcmus.dto.request.OpenAIMessage;
import com.hcmus.dto.response.OpenAIResponse;
import com.hcmus.exception.NotFoundException;
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
    public ChatRoom createChatRoom(String userId) {
        return chatRoomRepository.save(ChatRoom.builder()
                .name("Chưa có tiêu đề")
                .userId(userId)
                .build());
    }

    private List<OpenAIMessage> createOpenAIConversationOfRoom(String roomId) {
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
    public ChatMessage createChatMessage(ChatRequest chatRequest) {
        var chatMessage = chatMessageRepository.save(ChatMessage.builder()
                .userId(chatRequest.getUserId())
                .roomId(chatRequest.getRoomId())
                .text(chatRequest.getText())
                .build());

        var chatRoom = chatRoomRepository.findById(chatRequest.getRoomId()).orElseThrow(NotFoundException::new);

        if (!chatRoom.getNamed()) {
            chatRoom.setName(chatRequest.getText());
            chatRoom.setNamed(true);
        }

        chatMessageRepository.save(chatMessage);
        chatRoomRepository.save(chatRoom);

        return chatMessage;
    }

    @Transactional
    public ChatMessage createChatbotResponseOfRoom(String roomId) {
        List<OpenAIMessage> openAIMessages = createOpenAIConversationOfRoom(roomId);

        OpenAIResponse openAIResponse = openAIService.chatWithContext(openAIMessages);

        ChatMessage openAIMessage = ChatMessage.builder()
                .userId(GeneralConstant.CHATBOT_USER_ID)
                .roomId(roomId)
                .text(openAIResponse.getText())
                .build();

        return chatMessageRepository.save(openAIMessage);
    }

    private List<ChatRoom> findChatRoomsOfUserByDate(String userId, LocalDate date) {
        Instant startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        return chatRoomRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAt(userId, startOfDay, endOfDay);
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

        return stringBuilder.toString();
    }

    private String buildTranscriptFromChatMessagesOfRooms(List<String> roomIds) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < roomIds.size(); i++) {
            stringBuilder.append(String.format("Cuộc trò chuyện thứ %d:\n", i + 1));
            List<ChatMessage> messages = chatMessageRepository.findAllByRoomId(roomIds.get(i));
            stringBuilder.append(buildTranscriptFromChatMessages(messages));
        }

        return stringBuilder.toString();
    }

    private String summarizeChatMessagesOfRooms(List<String> roomIds) {
        String promptMessage = SUMMARY_PROMPT + buildTranscriptFromChatMessagesOfRooms(roomIds);
        OpenAIResponse response = openAIService.chatWithSingleMessage(
                OpenAIChatRequest.builder().message(promptMessage).build());
        return response.getText();
    }

    @Transactional
    public String summarizeChatMessagesOfUserByDate(String userId, LocalDate date) {
        List<ChatRoom> chatRooms = findChatRoomsOfUserByDate(userId, date);
        List<String> chatRoomIds = chatRooms.stream().map(ChatRoom::getId).toList();

        String summarized = summarizeChatMessagesOfRooms(chatRoomIds);
        List<ChatSummary> chatSummaries = chatSummaryRepository.findByUserIdAndDate(userId, date);
        Integer chatSummaryId = chatSummaries.isEmpty() ? null : chatSummaries.get(0).getId();
        chatSummaryRepository.save(
                ChatSummary.builder().id(chatSummaryId).date(date).text(summarized).userId(userId).build());

        return summarized;
    }

    public String getSummarized(String userId, LocalDate date) {
        List<ChatSummary> chatSummaries = chatSummaryRepository.findByUserIdAndDate(userId, date);
        return chatSummaries.isEmpty() ? null : chatSummaries.getFirst().getText();
    }

    public List<ChatRoom> getChatRoomsOfUser(String userId) {
        return chatRoomRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<ChatMessage> findChatMessagesOfRoom(String roomId) {
        return chatMessageRepository.findByRoomId(roomId);
    }
}
