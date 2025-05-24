package com.hcmus.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "oauth.google")
@Configuration
public class GoogleProperties {
	private String clientId;
	private String clientSecret;
}
