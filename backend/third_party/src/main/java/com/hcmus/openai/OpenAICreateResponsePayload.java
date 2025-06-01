package com.hcmus.openai;

import lombok.Builder;

@Builder
public record OpenAICreateResponsePayload(
        String model,
        Object input
) {

}
