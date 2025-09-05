package com.example.hrmanagement.dto;
import lombok.Data;

@Data
public class UserRegisterRequest {
    String username;
    String password;
    String role ;
    Long employeeId;
}
