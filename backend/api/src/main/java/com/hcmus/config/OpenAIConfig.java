package com.hcmus.config;

import com.hcmus.openai.OpenAIClient;
import com.hcmus.openai.OpenAIProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {
    @Bean
    @ConfigurationProperties(prefix = "open-ai")
    public OpenAIProperties openAIProperties() {
        return new OpenAIProperties();
    }

    @Bean
    public OpenAIClient openAIClient(OpenAIProperties openAIProperties) {
        return new OpenAIClient(openAIProperties);
    }
}
