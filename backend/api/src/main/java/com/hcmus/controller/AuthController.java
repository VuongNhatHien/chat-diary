package com.hcmus.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hcmus.config.ChatDiaryUserDetails;
import com.hcmus.dto.request.LoginRequest;
import com.hcmus.dto.request.RegisterRequest;
import com.hcmus.dto.response.ApiResponse;
import com.hcmus.dto.response.GoogleOAuthTokenResponse;
import com.hcmus.dto.response.LoginResponse;
import com.hcmus.service.AuthService;
import com.hcmus.service.GoogleOAuthService;
import com.hcmus.service.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;
    private final GoogleOAuthService googleOauthService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> register(@RequestBody @Valid RegisterRequest registerRequest) {
        authService.signup(registerRequest);

        return ApiResponse.created();
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LoginResponse> authenticate(@RequestBody @Valid LoginRequest loginRequest) {
        ChatDiaryUserDetails authenticatedUser = authService.login(loginRequest);

        String jwtToken = jwtService.generateToken(authenticatedUser.getUsername());

        LoginResponse response = LoginResponse.builder()
            .token(jwtToken)
            .build();

        return ApiResponse.created(response);
    }

    @GetMapping("/google-login")
    public ApiResponse<LoginResponse> googleLogin(@RequestParam("code") String authorizationCode)
        throws
        GeneralSecurityException,
        IOException {
        GoogleOAuthTokenResponse tokenResponse = googleOauthService.getGoogleOAuthToken(
            authorizationCode);

        String jwtToken = jwtService.decodeGoogleToken(tokenResponse.getIdToken());

        return ApiResponse.ok(new LoginResponse(jwtToken));
    }
}
