package com.example.hrmanagement.service;

import com.example.hrmanagement.dto.EmployeeRequest;
import com.example.hrmanagement.dto.EmployeeResponse;
import com.example.hrmanagement.models.*;
import com.example.hrmanagement.repository.DepartmentRepository;
import com.example.hrmanagement.repository.EmployeeRepository;
import com.example.hrmanagement.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final StorageService storageService;
    private final S3Presigner s3Presigner;

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket}")
    String bucketName;

    public void insertEmployee(EmployeeRequest request, MultipartFile file) {
        Employee employee = new Employee();
        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setGender(request.getGender());
        employee.setBirthDate(request.getBirthDate());
        employee.setHireDate(request.getHireDate());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setSalary(request.getSalary());
        employee.setStatus(request.getStatus());

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        employee.setDepartment(department);

        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new RuntimeException("Position not found"));
        employee.setPosition(position);
        if (file != null && !file.isEmpty()) {
            String profilePictureUrl = storageService.uploadFile(file, null);
            employee.setProfilePictureUrl(profilePictureUrl);
        }
        employeeRepository.save(employee);
    }

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAllByOrderByEmployeeIdAsc().stream()
                .map(entity -> {
                    EmployeeResponse response = EmployeeResponse.fromEntity(entity);
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

    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        EmployeeResponse response = EmployeeResponse.fromEntity(employee);

        if (response.getProfilePictureUrl() != null) {
            String bucketUrlPrefix = "https://hrmanagement-personal-project.s3.ap-southeast-1.amazonaws.com/";

            String key = response.getProfilePictureUrl().startsWith(bucketUrlPrefix)
                    ? response.getProfilePictureUrl().substring(bucketUrlPrefix.length())
                    : response.getProfilePictureUrl();

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)  // ชื่อ bucket ของคุณ
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

            response.setPresignedRequestUrl(presignedRequest.url().toString());
        }

        return response;
    }

    // UPDATE
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request, MultipartFile file) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setGender(request.getGender());
        employee.setBirthDate(request.getBirthDate());
        employee.setHireDate(request.getHireDate());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setSalary(request.getSalary());
        employee.setStatus(request.getStatus());

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new RuntimeException("Position not found"));

        employee.setDepartment(department);
        employee.setPosition(position);

        if (file != null && !file.isEmpty()) {
            // อัปโหลดไฟล์ใหม่และลบไฟล์เก่า
            String oldUrl = employee.getProfilePictureUrl();
            String newUrl = storageService.uploadFile(file, oldUrl);
            employee.setProfilePictureUrl(newUrl);
            System.out.println("newUrl : " + newUrl);
            System.out.println("อัปโหลดไฟล์ใหม่และลบไฟล์เก่า : " + employee);
        } else if (request.getProfilePictureUrl() == null && file == null || (file != null && file.isEmpty())) {
            // file เป็น null หรือว่าง ให้ลบไฟล์เก่าใน S3 และตั้งเป็น null
            String oldUrl = employee.getProfilePictureUrl();
            if (oldUrl != null && !oldUrl.isEmpty()) {
                storageService.deleteFile(oldUrl);
            }
            employee.setProfilePictureUrl(null);
            System.out.println("ProfilePictureUrl : " + request.getProfilePictureUrl());
            System.out.println("file เป็น null : " + employee);
        } else if (request.getProfilePictureUrl() != null && !request.getProfilePictureUrl().isEmpty()) {
            // อัปเดต URL ถ้ามีใน request
            employee.setProfilePictureUrl(request.getProfilePictureUrl());
            System.out.println("อัปเดต URL ถ้ามีใน request : " + employee);
        }
        return EmployeeResponse.fromEntity(employeeRepository.save(employee));
    }

    // DELETE
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with ID: " + id);
        }

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        if (employee.getProfilePictureUrl() != null && !employee.getProfilePictureUrl().isEmpty()) {
            storageService.deleteFile(employee.getProfilePictureUrl()); // ลบ S3
        }

        employeeRepository.delete(employee); // ลบ DB
    }
}
