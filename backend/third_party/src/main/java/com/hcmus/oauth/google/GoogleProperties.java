package com.hcmus.oauth.google;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleProperties {
    private String clientId;
    private String clientSecret;
    private String frontendUrl;
    private String redirectUri;
}
