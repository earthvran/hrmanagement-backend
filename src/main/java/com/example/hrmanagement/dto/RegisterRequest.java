package com.example.hrmanagement.dto;

public record RegisterRequest(
        String username,
        String password,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {}
