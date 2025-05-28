package com.hcmus.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "oauth.google")
@Configuration
public class GoogleProperties {

    private String clientId;
    private String clientSecret;
}
