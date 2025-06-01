package com.hcmus.controller;

import com.hcmus.config.ChatDiaryUserDetails;
import com.hcmus.constant.GeneralConstant;
import com.hcmus.dto.request.LoginRequest;
import com.hcmus.dto.request.RegisterRequest;
import com.hcmus.dto.response.ApiResponse;
import com.hcmus.dto.response.LoginResponse;
import com.hcmus.oauth.google.GoogleClient;
import com.hcmus.oauth.google.GoogleOAuthTokenRaw;
import com.hcmus.service.AuthService;
import com.hcmus.service.GoogleService;
import com.hcmus.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;
    private final GoogleService googleService;
    private final GoogleClient googleClient;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> register(@RequestBody @Valid RegisterRequest registerRequest) {
        authService.signup(registerRequest);

        return ApiResponse.created();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LoginResponse> authenticate(@RequestBody @Valid LoginRequest loginRequest,
                                                   HttpServletResponse httpServletResponse) {
        ChatDiaryUserDetails authenticatedUser = authService.login(loginRequest);

        String accessToken = jwtService.generateToken(authenticatedUser.getUsername());

        authService.setCookie(httpServletResponse, GeneralConstant.ACCESS_TOKEN_KEY, accessToken);

        return ApiResponse.created();
    }

    @PostMapping("/google/login")
    public ApiResponse<Void> googleLogin(@RequestParam("code") String authorizationCode, HttpServletResponse httpServletResponse) {
        GoogleOAuthTokenRaw tokenResponse = googleClient.getGoogleOAuthToken(authorizationCode);

        String jwtToken = googleService.generateTokenFromGoogleIdToken(tokenResponse.getIdToken());

        authService.setCookie(httpServletResponse, GeneralConstant.ACCESS_TOKEN_KEY, jwtToken);

        return ApiResponse.ok();
    }

    @PutMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse httpServletResponse) {
        authService.removeCookie(httpServletResponse, GeneralConstant.ACCESS_TOKEN_KEY);

        return ApiResponse.ok();
    }
}
