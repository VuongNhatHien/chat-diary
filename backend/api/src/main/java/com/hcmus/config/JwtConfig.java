package com.hcmus.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmus.config.property.JwtProperties;
import com.hcmus.ctypto.rsa.RSAJwtDecoder;
import com.hcmus.ctypto.rsa.RSAJwtEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

	private final JwtProperties jwtProperties;

	@Bean
	public JwtEncoder jwtEncoder(ObjectMapper objectMapper) {
		return new RSAJwtEncoder(jwtProperties.getRsaPrivateKey(), objectMapper);
	}

	@Bean
	public JwtDecoder jwtDecoder(ObjectMapper objectMapper) {
		return new RSAJwtDecoder(jwtProperties.getRsaPublicKey(), objectMapper);
	}
}
