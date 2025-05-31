package com.hcmus.config;

import com.hcmus.config.property.JwtProperties;
import com.hcmus.ctypto.rsa.RSAJwtDecoder;
import com.hcmus.ctypto.rsa.RSAJwtEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtProperties jwtProperties;

    @Bean
    public JwtEncoder jwtEncoder() {
        return new RSAJwtEncoder(jwtProperties.getRsaPrivateKey());
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return new RSAJwtDecoder(jwtProperties.getRsaPublicKey());
    }
}
