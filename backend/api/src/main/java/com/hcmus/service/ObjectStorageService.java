package com.hcmus.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ObjectStorageService {
    private final RestTemplate restTemplate = new RestTemplate();

    public String getSignedPutUrl(String key) {
        return restTemplate.getForObject("http://localhost:8081/sign/put-url?key=" + key, String.class);
    }

    public String getSignedGetUrl(String key) {
        return restTemplate.getForObject("http://localhost:8081/sign/get-url?key=" + key, String.class);
    }
}
