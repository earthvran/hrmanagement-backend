package com.example.hrmanagement.controller;

import com.example.hrmanagement.dto.*;
import com.example.hrmanagement.service.AuthService;
import com.example.hrmanagement.service.UserService;
import com.example.hrmanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/userRegister")
    public ResponseEntity<String> register(@RequestBody UserRegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PutMapping("/updateUser/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody UserRegisterRequest request) {
        userService.updateUser(id, request);
        return "User updated successfully";
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        return ResponseEntity.ok(authService.getProfile(username));
    }
}
