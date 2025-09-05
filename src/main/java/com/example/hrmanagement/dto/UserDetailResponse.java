package com.example.hrmanagement.dto;
import com.example.hrmanagement.models.User;
import lombok.Data;

@Data
public class UserDetailResponse {
    String username;
    String role ;
    Long employeeId;

    public UserDetailResponse(String username, String role, Long employeeId) {
        this.username = username;
        this.role = role;
        this.employeeId = employeeId;
    }

    public static UserDetailResponse fromEntity(User user) {
        return new UserDetailResponse(
                user.getUsername(),
                user.getRole().name(),
                user.getEmployee() != null ? user.getEmployee().getEmployeeId() : null
        );
    }
}
