package com.hcmus.ctypto.rsa;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtEncodingException;

import java.security.PrivateKey;

import com.hcmus.ctypto.base64url.Base64UrlEncoder;

public class RSAJwtEncoder implements JwtEncoder {
    private final PrivateKey privateKey;
    private final ObjectMapper objectMapper;

    public RSAJwtEncoder(PrivateKey privateKey, ObjectMapper objectMapper) {
        this.privateKey = privateKey;
        this.objectMapper = objectMapper;
    }

    @Override
    public Jwt encode(JwtEncoderParameters parameters) throws JwtEncodingException {
        try {
            JwsHeader header = parameters.getJwsHeader();
            JwtClaimsSet claims = parameters.getClaims();

            assert header != null;
            String headerJson = objectMapper.writeValueAsString(header.getHeaders());
            String payloadJson = objectMapper.writeValueAsString(claims.getClaims());

            String headerBase64 = Base64UrlEncoder.encode(headerJson);
            String payloadBase64 = Base64UrlEncoder.encode(payloadJson);

            String signingInput = headerBase64 + "." + payloadBase64;
            byte[] signature = RSA.sign(signingInput, privateKey);
            String signatureBase64 = Base64UrlEncoder.encode(signature);

            String token = signingInput + "." + signatureBase64;

            return new Jwt(token, claims.getIssuedAt(), claims.getExpiresAt(), header.getHeaders(), claims.getClaims());

        } catch (Exception e) {
            throw new JwtEncodingException("Failed to encode JWT", e);
        }
    }
}
