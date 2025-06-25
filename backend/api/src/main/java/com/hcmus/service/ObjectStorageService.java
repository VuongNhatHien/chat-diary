package com.hcmus.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class ObjectStorageService {
    private final RestTemplate restTemplate = new RestTemplate();

    public void upload(String signedUrl, MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())));

        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);

        restTemplate.exchange(signedUrl, HttpMethod.PUT, entity, String.class);
    }
}
