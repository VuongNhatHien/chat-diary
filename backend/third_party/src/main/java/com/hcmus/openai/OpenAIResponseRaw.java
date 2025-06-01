package com.hcmus.openai;

import lombok.Builder;

import java.util.List;

@Builder
public record OpenAIResponseRaw(
        String id,
        List<Output> output
) {

    public record Output(
            List<Content> content
    ) {

        public record Content(
                String text
        ) {

        }
    }
}
