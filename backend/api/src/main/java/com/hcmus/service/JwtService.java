package com.hcmus.service;

import com.hcmus.oauth.google.GoogleProperties;
import com.hcmus.property.JwtProperties;
import com.hcmus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final GoogleProperties googleProperties;
    private final UserRepository userRepository;
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public String extractUsername(String token) {
        Jwt jwt = decoder.decode(token);
        return jwt.getSubject();
    }

    public String generateToken(String username) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusMillis(jwtProperties.getExpiration()))
                .subject(username)
                .build();

        var encoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(SignatureAlgorithm.RS256).build(), claims);

        return encoder.encode(encoderParameters).getTokenValue();
    }

    public boolean isTokenExpired(String token) {
        try {
            Jwt jwt = decoder.decode(token);
            Instant expiration = jwt.getExpiresAt();
            return expiration != null && expiration.isBefore(Instant.now());
        } catch (JwtException e) {
            return true;
        }
    }
}
