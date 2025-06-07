package com.hcmus.controller;

import com.hcmus.service.SignatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign")
public class SignedUrlController {
    private final SignatureService signatureService;

    public SignedUrlController(SignatureService signatureService) {
        this.signatureService = signatureService;
    }

    @GetMapping("/get-url")
    public ResponseEntity<String> getSignedDownloadUrl(@RequestParam String key) throws Exception {
        return ResponseEntity.ok(signatureService.generateSignedUrl(key, "GET", 10));
    }

    @GetMapping("/put-url")
    public ResponseEntity<String> getSignedUploadUrl(@RequestParam String key) throws Exception {
        return ResponseEntity.ok(signatureService.generateSignedUrl(key, "PUT", 10));
    }
}

