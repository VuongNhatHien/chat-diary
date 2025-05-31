package com.hcmus.ctypto.rsa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmus.ctypto.base64url.Base64UrlDecoder;
import com.hcmus.utils.ObjectMapperFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Map;

import static com.hcmus.ctypto.rsa.RSA.verifySignature;

public class RSAJwtDecoder implements JwtDecoder {

    private final RSAPublicKey publicKey;
    private final ObjectMapper objectMapper = ObjectMapperFactory.create();

    public RSAJwtDecoder(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new JwtException("Invalid JWT format");
            }

            String encodedHeader = parts[0];
            String encodedPayload = parts[1];
            String encodedSignature = parts[2];

            String headerJson = Base64UrlDecoder.decode(encodedHeader);
            String payloadJson = Base64UrlDecoder.decode(encodedPayload);
            byte[] signatureBytes = Base64UrlDecoder.decodeToBytes(encodedSignature);

            String signingInput = encodedHeader + "." + encodedPayload;
            verifySignature(signingInput, signatureBytes, publicKey);

            Map<String, Object> headers = objectMapper.readValue(headerJson, new TypeReference<>() {
            });
            Map<String, Object> claims = objectMapper.readValue(payloadJson, new TypeReference<>() {
            });

            Instant issuedAt = claims.containsKey("iat") ? Instant.ofEpochSecond(((Number) claims.get("iat")).longValue()) : null;
            Instant expiresAt = claims.containsKey("exp") ? Instant.ofEpochSecond(((Number) claims.get("exp")).longValue()) : null;

            return new Jwt(token, issuedAt, expiresAt, headers, claims);

        } catch (Exception e) {
            throw new JwtException("Failed to decode or verify JWT", e);
        }
    }
}
