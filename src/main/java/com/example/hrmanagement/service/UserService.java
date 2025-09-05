package com.example.hrmanagement.service;

import com.example.hrmanagement.dto.UserDetailResponse;
import com.example.hrmanagement.dto.UserRegisterRequest;
import com.example.hrmanagement.dto.UserResponse;
import com.example.hrmanagement.models.Employee;
import com.example.hrmanagement.models.Role;
import com.example.hrmanagement.models.User;
import com.example.hrmanagement.repository.EmployeeRepository;
import com.example.hrmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void register(UserRegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));

        // ดึง Employee จาก employeeId แล้วค่อย set เข้า user
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        user.setEmployee(employee);

        userRepository.save(user);
    }

    public void updateUser(Long userId, UserRegisterRequest request) {
        // ดึง User ที่จะ update
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // อัปเดตข้อมูล
        user.setUsername(request.getUsername());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            user.setPasswordHash(encodedPassword);
        }

        user.setRole(Enum.valueOf(Role.class, request.getRole()));

        // ดึง Employee จาก employeeId และ set
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + request.getEmployeeId()));

        user.setEmployee(employee);

        userRepository.save(user);
    }
    // GET ALL USERS
    public List<UserDetailResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDetailResponse::fromEntity)
                .collect(Collectors.toList());
    }
    // GET USER BY ID
    public UserDetailResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserDetailResponse.fromEntity(user);
    }

    // DELETE USER BY ID
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

}

