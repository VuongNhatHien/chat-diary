package com.hcmus.rest_client.openai;

import com.hcmus.config.property.OpenAIProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenAIClient {

    private final RestClient restClient;
    private final OpenAIProperties openAIProperties;

    public OpenAIClient(OpenAIProperties openAIProperties, RestClient.Builder restClientBuilder) {
        this.openAIProperties = openAIProperties;
        this.restClient = restClientBuilder
                .baseUrl(openAIProperties.getDomain())
                .defaultHeader("Authorization", "Bearer " + openAIProperties.getSecretKey())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public OpenAIResponseRaw createResponse(Object input) {
        var requestBody = OpenAICreateResponseRequest.builder()
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
