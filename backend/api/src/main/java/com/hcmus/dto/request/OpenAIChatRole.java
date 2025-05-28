package com.hcmus.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum OpenAIChatRole {
	ASSISTANT("assistant"), USER("user");

	private final String value;

	OpenAIChatRole(String value) {
		this.value = value;
	}

	@JsonCreator
	public static OpenAIChatRole fromValue(String value) {
		for (OpenAIChatRole role : values()) {
			if (role.value.equalsIgnoreCase(value)) {
				return role;
			}
		}
		throw new IllegalArgumentException("Unknown status: " + value);
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
