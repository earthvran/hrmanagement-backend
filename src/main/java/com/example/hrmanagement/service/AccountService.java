package com.example.hrmanagement.service;

import com.example.hrmanagement.dto.AccountRequest;
import com.example.hrmanagement.dto.AccountResponse;
import com.example.hrmanagement.dto.EmployeeResponse;
import com.example.hrmanagement.models.Employee;
import com.example.hrmanagement.models.Role;
import com.example.hrmanagement.models.User;
import com.example.hrmanagement.repository.EmployeeRepository;
import com.example.hrmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    String bucketName;

    public void createUser(AccountRequest request) {
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

    public void updateUser(Long userId, AccountRequest request) {
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

    public List<AccountResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(entity -> {
                    AccountResponse response = AccountResponse.fromEntity(entity);
                    if (response.getProfilePictureUrl() != null) {
                        // กำหนด prefix ของ bucket URL ที่ใช้เก็บไฟล์จริง
                        String bucketUrlPrefix = "https://hrmanagement-personal-project.s3.ap-southeast-1.amazonaws.com/";

                        // ตัดเฉพาะ key ของไฟล์ออกจาก URL เดิม
                        String key = response.getProfilePictureUrl().startsWith(bucketUrlPrefix)
                                ? response.getProfilePictureUrl().substring(bucketUrlPrefix.length())
                                : response.getProfilePictureUrl();

                        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                .bucket(bucketName)  // สมมติ bucketName กำหนดใน class หรือ config แล้ว
                                .key(key)           // ใช้ key ที่ตัดออกมา
                                .build();

                        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                                .signatureDuration(Duration.ofMinutes(15))
                                .getObjectRequest(getObjectRequest)
                                .build();

                        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
                        response.setPresignedRequestUrl(presignedRequest.url().toString());

                        System.out.println("presignedRequestUrl: " + presignedRequest.url().toString());
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }

    // DELETE USER BY ID
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ตัดความสัมพันธ์ก่อนลบ
        Employee employee = user.getEmployee();
        if (employee != null) {
            employee.setUser(null); // ตัดจากฝั่ง Employee
            user.setEmployee(null); // ตัดจากฝั่ง User
        }

        userRepository.delete(user);
    }
}
