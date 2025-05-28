package com.hcmus.controller;

import com.hcmus.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ApiResponse.ok("Hello World");
    }
}
