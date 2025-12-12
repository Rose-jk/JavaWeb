package com.snackshop.service;

import com.snackshop.dto.LoginRequest;
import com.snackshop.dto.RegisterRequest;
import com.snackshop.model.User;
import com.snackshop.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public Map<String, Object> login(LoginRequest request) {
        // 认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成token
        User user = (User) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(user);

        // 更新最后登录时间
        userService.updateLastLogin(user.getEmail());

        // 构建响应数据
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);

        return response;
    }

    public User register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setBirthdate(request.getBirthdate() != null ? request.getBirthdate() : LocalDate.now().minusYears(20));

        return userService.registerUser(user);
    }
}