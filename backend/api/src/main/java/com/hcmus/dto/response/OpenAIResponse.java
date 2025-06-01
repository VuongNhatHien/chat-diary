package com.hcmus.dto.response;

import com.hcmus.openai.OpenAIResponseRaw;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenAIResponse {

    private String id;
    private String text;

    public static OpenAIResponse fromRaw(OpenAIResponseRaw response) {
        return OpenAIResponse.builder()
                .id(response.id())
                .text(response.output().getFirst().content().getFirst().text())
                .build();
    }
}
