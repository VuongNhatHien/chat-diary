package com.hcmus.controller;

import com.hcmus.exception.BadRequestException;
import com.hcmus.service.SignatureService;
import com.hcmus.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

@RequestMapping("/pre-signed-url")
@RestController
public class FileAccessController {
    private final StorageService storageService;
    private final SignatureService signatureService;

    public FileAccessController(StorageService storageService, SignatureService signatureService) {
        this.storageService = storageService;
        this.signatureService = signatureService;
    }

    @PutMapping("/{bucket}/{key:.+}")
    public ResponseEntity<?> upload(@PathVariable String bucket, @PathVariable String key, HttpServletRequest request) {
        signatureService.verifySignature(request, "PUT", bucket + "/" + key);

        try {
            storageService.save(bucket, key, request.getInputStream());
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
        return ResponseEntity.ok("Uploaded");
    }

    @GetMapping("/{bucket}/{key:.+}")
    public ResponseEntity<?> download(@PathVariable String bucket, @PathVariable String key, HttpServletRequest request) {
        signatureService.verifySignature(request, "GET", bucket + "/" + key);

        try {
            File file = storageService.read(bucket, key);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(contentType))
                    .contentLength(file.length())
                    .body(resource);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
