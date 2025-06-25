package com.hcmus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SignedUrlService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String endpoint;

    public SignedUrlService(@Value("${domain.object-storage}") String domain) {
        this.endpoint = "http://" + domain + "/sign";
    }

    private String paramsUrl(String bucket, String key) {
        return "?bucket=" + bucket + "&key=" + key;
    }

    public String getSignedGetUrl(String bucket, String key) {
        return restTemplate.getForObject(endpoint + "/get-url" + paramsUrl(bucket, key), String.class);
    }

    public String getSignedPutUrl(String bucket, String key) {
        return restTemplate.getForObject(endpoint + "/put-url" + paramsUrl(bucket, key), String.class);
    }
}
