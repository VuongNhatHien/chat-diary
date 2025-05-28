package com.hcmus.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "open-ai")
@Configuration
public class OpenAIProperties {

    private String domain;
    private String secretKey;
    private String model;
}
