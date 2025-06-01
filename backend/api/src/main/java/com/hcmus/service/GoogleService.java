package com.hcmus.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hcmus.model.Role;
import com.hcmus.model.User;
import com.hcmus.oauth.google.GoogleProperties;
import com.hcmus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleService {
    private final GoogleProperties googleProperties;
    private final UserRepository userRepository;
    private final JwtService jwtService;

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

        return jwtService.generateToken(user.getId());
    }
}
