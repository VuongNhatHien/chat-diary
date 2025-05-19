package com.hcmus.chatdiary.controller;

import com.hcmus.chatdiary.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ApiResponse.ok("Hello World");
    }
}
