package com.hcmus.controller;

import com.hcmus.service.ObjectStorageService;
import com.hcmus.service.SignedUrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/image")
public class ImageController {
    private final SignedUrlService storageService;
    private final ObjectStorageService objectStorageService;
    private final String bucket = "images";

    public ImageController(SignedUrlService signedUrlService, ObjectStorageService storageService) {
        this.storageService = signedUrlService;
        this.objectStorageService = storageService;
    }

    @GetMapping("/get")
    public ResponseEntity<?> download(@RequestParam("key") String key) {
        String signedUrl = storageService.getSignedGetUrl(bucket, key);
        return ResponseEntity.ok(signedUrl);
    }

    @PutMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws Exception {
        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();

        String signedUrl = storageService.getSignedPutUrl(bucket, key);
        objectStorageService.upload(signedUrl, file);

        return ResponseEntity.ok("Uploaded with key: " + key);
    }
}
