package com.hcmus.controller;

import com.hcmus.service.ObjectStorageService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final ObjectStorageService storageService;
    private final RestTemplate restTemplate = new RestTemplate();

    public ImageController(ObjectStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/get")
    public ResponseEntity<?> download(@RequestParam("key") String key) throws Exception {
        String signedUrl = storageService.getSignedGetUrl(key);
        return restTemplate.getForEntity(signedUrl, String.class);
    }

    @PutMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws Exception {
        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();

        String signedUrl = storageService.getSignedPutUrl(key);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())));

        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);

        restTemplate.exchange(signedUrl, HttpMethod.PUT, entity, String.class);

        return ResponseEntity.ok("Uploaded with key: " + key);
    }
}
