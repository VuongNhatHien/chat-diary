package com.hcmus.openai;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class OpenAIClient {

    private final RestClient restClient;
    private final OpenAIProperties openAIProperties;

    public OpenAIClient(OpenAIProperties openAIProperties) {
        this.openAIProperties = openAIProperties;
        this.restClient = RestClient.builder()
                .baseUrl(openAIProperties.getDomain())
                .defaultHeader("Authorization", "Bearer " + openAIProperties.getSecretKey())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public OpenAIResponseRaw createResponse(Object input) {
        var requestBody = OpenAICreateResponsePayload.builder()
                .model(openAIProperties.getModel())
                .input(input)
                .build();

        return restClient.post()
                .uri("/responses")
                .body(requestBody)
                .retrieve()
                .body(OpenAIResponseRaw.class);
    }
}
