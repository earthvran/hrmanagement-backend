package com.example.hrmanagement.mapper;

import com.example.hrmanagement.dto.UserResponse;
import com.example.hrmanagement.models.User;

public class UserMapper {

    public static UserResponse toDto(User user) {
        String fullName = user.getEmployee() != null
                ? user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName()
                : "-";

        return new UserResponse(
                user.getUsername(),
                user.getRole().name(),
                fullName,
                user.getEmployee() != null ? user.getEmployee().getEmail() : "-"
        );
    }
}
