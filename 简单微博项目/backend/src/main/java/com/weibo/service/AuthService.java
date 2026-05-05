package com.weibo.service;

import com.weibo.dto.ApiResponse;
import com.weibo.dto.LoginRequest;
import com.weibo.dto.RegisterRequest;
import com.weibo.entity.User;
import com.weibo.repository.UserRepository;
import com.weibo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    public ApiResponse<?> register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ApiResponse.error("Username already taken");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return ApiResponse.success("User registered successfully");
    }
    
    public ApiResponse<?> login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(request.getUsername());
        User user = userRepository.findByUsername(request.getUsername()).get();
        Map<String, Object> data = new HashMap<>();
        data.put("token", jwt);
        data.put("user", user);
        return ApiResponse.success(data);
    }
}