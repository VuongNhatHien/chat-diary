package com.hcmus.service;

import com.hcmus.dto.request.OpenAIChatRequest;
import com.hcmus.dto.request.OpenAIMessage;
import com.hcmus.dto.response.OpenAIResponse;
import com.hcmus.rest_client.openai.OpenAIClient;
import com.hcmus.rest_client.openai.OpenAIResponseRaw;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final OpenAIClient openAIClient;

    public OpenAIResponse chatWithSingleMessage(OpenAIChatRequest request) {
        OpenAIResponseRaw responseRaw = openAIClient.createResponse(request.getMessage());
        return OpenAIResponse.fromRaw(responseRaw);
    }

    public OpenAIResponse chatWithContext(List<OpenAIMessage> request) {
        OpenAIResponseRaw responseRaw = openAIClient.createResponse(request);
        return OpenAIResponse.fromRaw(responseRaw);
    }
}
