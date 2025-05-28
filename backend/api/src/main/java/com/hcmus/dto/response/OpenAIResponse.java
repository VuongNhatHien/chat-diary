package com.hcmus.dto.response;

import com.hcmus.rest_client.openai.OpenAIResponseRaw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
