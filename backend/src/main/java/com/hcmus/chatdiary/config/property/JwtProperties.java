package com.hcmus.chatdiary.config.property;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
	private long expiration;
	private RSAPrivateKey rsaPrivateKey;
	private RSAPublicKey rsaPublicKey;
}
