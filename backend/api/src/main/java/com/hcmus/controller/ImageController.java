package com.hcmus.controller;

import com.hcmus.config.ChatDiaryUserDetails;
import com.hcmus.dto.response.ApiResponse;
import com.hcmus.service.ObjectStorageService;
import com.hcmus.service.SignedUrlService;
import com.hcmus.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/image")
public class ImageController {
    private final SignedUrlService storageService;
    private final ObjectStorageService objectStorageService;
    private final String bucket = "images";
    private final UserService userService;

    public ImageController(SignedUrlService signedUrlService, ObjectStorageService storageService, UserService userService) {
        this.storageService = signedUrlService;
        this.objectStorageService = storageService;
        this.userService = userService;
    }

    @GetMapping("/get")
    public ApiResponse<String> download(@RequestParam("key") String key) {
        String signedUrl = storageService.getSignedGetUrl(bucket, key);
        return ApiResponse.ok(signedUrl);
    }

    @PutMapping("/upload")
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file, Principal principal) throws Exception {
        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();

        String signedUrl = storageService.getSignedPutUrl(bucket, key);
        objectStorageService.upload(signedUrl, file);

        userService.updateUserAvatar(principal.getName(), key);

        return ApiResponse.ok("Uploaded with key: " + key);
    }
}
