package com.hcmus.controller;

import com.hcmus.service.SignatureService;
import com.hcmus.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileAccessController {
    private final StorageService storageService;
    private final SignatureService signatureService;

    public FileAccessController(StorageService storageService, SignatureService signatureService) {
        this.storageService = storageService;
        this.signatureService = signatureService;
    }

    @PutMapping("/{bucket}/{key:.+}")
    public ResponseEntity<?> upload(@PathVariable String bucket, @PathVariable String key, HttpServletRequest request) throws Exception {
        if (signatureService.verifySignature(request, "PUT", bucket + "/" + key)) {
            return ResponseEntity.status(403).body("Invalid signature");
        }
        storageService.save(bucket, key, request.getInputStream());
        return ResponseEntity.ok("Uploaded");
    }

    @GetMapping("/{bucket}/{key:.+}")
    public ResponseEntity<?> download(@PathVariable String bucket, @PathVariable String key, HttpServletRequest request) throws Exception {
        if (signatureService.verifySignature(request, "GET", bucket + "/" + key)) {
            return ResponseEntity.status(403).body("Invalid signature");
        }
        return ResponseEntity.ok(storageService.read(bucket, key));
    }
}
