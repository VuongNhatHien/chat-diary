package com.hcmus.oauth.google;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class GoogleClient {
    private final GoogleProperties googleProperties;

    public GoogleClient(GoogleProperties googleProperties) {
        this.googleProperties = googleProperties;
    }

    public GoogleOAuthTokenRaw getGoogleOAuthToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("code", code);
        params.add("redirect_uri", googleProperties.getRedirectUri());
        params.add("client_id", googleProperties.getClientId());
        params.add("client_secret", googleProperties.getClientSecret());
        params.add("scope",
                "openid https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");
        params.add("scope", "openid");
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params,
                httpHeaders);

        String url = "https://oauth2.googleapis.com/token";

        return restTemplate.postForObject(url, requestEntity, GoogleOAuthTokenRaw.class);
    }
}

