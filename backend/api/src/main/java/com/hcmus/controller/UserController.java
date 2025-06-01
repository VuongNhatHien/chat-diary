package com.hcmus.controller;

import com.hcmus.dto.response.ApiResponse;
import com.hcmus.model.User;
import com.hcmus.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/me")
    public ApiResponse<User> getMe() {
        return ApiResponse.ok(authService.getMe());
    }
}
