package com.hcmus.rest_client.openai;

import lombok.Builder;

@Builder
public record OpenAICreateResponseRequest(
	String model,
	Object input
) {
}
