package com.hcmus.controller;

import com.hcmus.service.SignatureService;
import com.hcmus.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;

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

        File file = storageService.read(bucket, key);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        String contentType = Files.probeContentType(file.toPath()); // tự đoán loại MIME
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .contentLength(file.length())
                .body(resource);
    }
}
