package com.snackshop.controller;

import com.snackshop.dto.ApiResponse;
import com.snackshop.dto.LoginRequest;
import com.snackshop.dto.RegisterRequest;
import com.snackshop.model.User;
import com.snackshop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@Valid @RequestBody LoginRequest request) {
        Map<String, Object> result = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("登录成功", result));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("注册成功", user));
    }

    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String email) {
        // 简单验证，实际应检查数据库
        boolean exists = email != null && email.contains("@");
        return ResponseEntity.ok(ApiResponse.success(!exists));
    }

    @GetMapping("/check-username")
    public ResponseEntity<ApiResponse<Boolean>> checkUsername(@RequestParam String username) {
        // 简单验证
        boolean exists = username != null && username.length() >= 2;
        return ResponseEntity.ok(ApiResponse.success(!exists));
    }
}