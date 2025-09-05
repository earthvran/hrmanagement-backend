package com.example.hrmanagement.dto;

public record UserResponse(
        String username,
        String role,
        String fullName,
        String email
) {}
