package com.hcmus.controller;

import static com.hcmus.constant.GeneralConstant.ACCESS_TOKEN_KEY;

import com.hcmus.config.ChatDiaryUserDetails;
import com.hcmus.dto.request.LoginRequest;
import com.hcmus.dto.request.RegisterRequest;
import com.hcmus.dto.response.ApiResponse;
import com.hcmus.dto.response.GoogleOAuthTokenResponse;
import com.hcmus.dto.response.LoginResponse;
import com.hcmus.service.AuthService;
import com.hcmus.service.GoogleOAuthService;
import com.hcmus.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;
    private final GoogleOAuthService googleOauthService;

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

        Cookie cookie = new Cookie(ACCESS_TOKEN_KEY, accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "None");

        httpServletResponse.addCookie(cookie);

        return ApiResponse.created();
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
