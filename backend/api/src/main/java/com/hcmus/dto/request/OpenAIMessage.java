package com.hcmus.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OpenAIMessage {

    OpenAIChatRole role;
    String content;
}
