package com.hcmus.rest_client.openai;

import java.util.List;

import lombok.Builder;

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
