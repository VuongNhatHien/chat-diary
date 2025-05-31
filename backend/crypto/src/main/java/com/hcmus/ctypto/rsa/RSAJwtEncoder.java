package com.hcmus.ctypto.rsa;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmus.ctypto.base64url.Base64UrlEncoder;
import com.hcmus.utils.ObjectMapperFactory;
import org.springframework.security.oauth2.jwt.*;

import java.security.PrivateKey;

public class RSAJwtEncoder implements JwtEncoder {
    private final PrivateKey privateKey;
    private final ObjectMapper objectMapper = ObjectMapperFactory.create();

    public RSAJwtEncoder(PrivateKey privateKey) {
        this.privateKey = privateKey;
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
