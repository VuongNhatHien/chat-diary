package com.hcmus.chatdiary.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hcmus.chatdiary.config.property.GoogleProperties;
import com.hcmus.chatdiary.config.property.JwtProperties;
import com.hcmus.chatdiary.model.Role;
import com.hcmus.chatdiary.model.User;
import com.hcmus.chatdiary.repository.UserRepository;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

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

    public String decodeGoogleToken(String token) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
            new JacksonFactory())
            .setAudience(Collections.singletonList(googleProperties.getClientId()))
            .build();

        GoogleIdToken idToken = verifier.verify(token);

        if (idToken == null) {
            return null;
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        String email = payload.getEmail();
        User user = new User();
        if (userRepository.findByEmail(email).isEmpty()) {
            String subject = payload.getSubject();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");

            user.setId(subject);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRoles(Role.USER.getValue());

            userRepository.save(user);
        } else {
            user = userRepository.findByEmail(email).get();
        }

        return generateToken(user.getId());
    }
}
