package com.hcmus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmus.dto.response.ApiResponse;

@RestController
public class TestController {

    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ApiResponse.ok("Hello World");
    }
}
