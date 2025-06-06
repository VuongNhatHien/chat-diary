package com.hcmus.service;

import org.springframework.stereotype.Service;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class StorageService {

    private final Path baseDir = Paths.get("data");

    public void save(String bucket, String key, InputStream inputStream) throws IOException {
        Path bucketPath = baseDir.resolve(bucket);
        Files.createDirectories(bucketPath);
        Path filePath = bucketPath.resolve(key);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public ResponseEntity<?> read(String bucket, String key) throws IOException {
        Path filePath = baseDir.resolve(bucket).resolve(key);
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        InputStream is = new FileInputStream(filePath.toFile());
        InputStreamResource resource = new InputStreamResource(is);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
