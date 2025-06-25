package com.hcmus.property;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "signature")
public class SignatureProperties {
    private String rsaPrivateKeyPath;
    private String rsaPublicKeyPath;

    private String rsaPrivateKey;
    private String rsaPublicKey;

    @PostConstruct
    public void loadPemFiles() throws IOException {
        this.rsaPrivateKey = readFileContent(rsaPrivateKeyPath);
        this.rsaPublicKey = readFileContent(rsaPublicKeyPath);
    }

    private String readFileContent(String path) throws IOException {
        Resource resource = new DefaultResourceLoader().getResource(path);
        return Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
    }
}

