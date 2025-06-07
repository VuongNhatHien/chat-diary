package com.hcmus.controller;

import com.hcmus.dto.response.ApiResponse;
import com.hcmus.property.ObjectStorageProperties;
import com.hcmus.service.SignatureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign")
public class SignedUrlController {
    private final SignatureService signatureService;
    @Value("${url.expiration}")
    private int expiration;

    public SignedUrlController(SignatureService signatureService) {
        this.signatureService = signatureService;
    }

    @GetMapping("/get-url")
    public ApiResponse<String> getSignedDownloadUrl(
        @RequestParam String bucket,
        @RequestParam String key
    ) {
        ObjectStorageProperties properties = fromParams(bucket);
        return ApiResponse.ok(signatureService.generateSignedUrl(properties, key, "GET", expiration));
    }

    @GetMapping("/put-url")
    public ApiResponse<String>  getSignedUploadUrl(
        @RequestParam String bucket,
        @RequestParam String key
    ) throws Exception {
        ObjectStorageProperties properties = fromParams(bucket);
        return ApiResponse.ok(signatureService.generateSignedUrl(properties, key, "PUT", expiration));
    }

    private ObjectStorageProperties fromParams(String bucket) {
        return ObjectStorageProperties.builder()
                .bucket(bucket)
                .build();
    }
}

