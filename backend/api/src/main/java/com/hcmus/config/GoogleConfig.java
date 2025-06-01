package com.hcmus.config;

import com.hcmus.oauth.google.GoogleClient;
import com.hcmus.oauth.google.GoogleProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleConfig {
    @ConfigurationProperties(prefix = "oauth.google")
    @Bean
    public GoogleProperties googleProperties() {
        return new GoogleProperties();
    }

    @Bean
    public GoogleClient googleClient(GoogleProperties googleProperties) {
        return new GoogleClient(googleProperties);
    }
}
