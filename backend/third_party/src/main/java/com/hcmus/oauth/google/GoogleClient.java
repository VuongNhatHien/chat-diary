package com.hcmus.oauth.google;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

public class GoogleClient {
    private final GoogleProperties googleProperties;
    private final RestClient restClient;

    public GoogleClient(GoogleProperties googleProperties) {
        this.googleProperties = googleProperties;
        this.restClient = RestClient.builder().build();
    }

    public GoogleOAuthTokenRaw getGoogleOAuthToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("redirect_uri", googleProperties.getRedirectUri());
        formData.add("client_id", googleProperties.getClientId());
        formData.add("client_secret", googleProperties.getClientSecret());
        formData.add("scope", "openid https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");
        formData.add("grant_type", "authorization_code");

        return restClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(GoogleOAuthTokenRaw.class);
    }

    public GoogleUserInfoRaw getGoogleUserInfo(String idToken) {
        return restClient.get()
                .uri("https://www.googleapis.com/oauth2/v3/tokeninfo", uriBuilder -> uriBuilder
                        .queryParam("id_token", idToken)
                        .build())
                .retrieve()
                .body(GoogleUserInfoRaw.class);
    }
}

