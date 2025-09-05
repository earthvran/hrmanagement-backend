package com.example.hrmanagement.dto;

import com.example.hrmanagement.models.EmploymentStatus;
import com.example.hrmanagement.models.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeRequest {
    private String employeeCode;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private String email;
    private String phoneNumber;
    private Long departmentId;
    private Long positionId;
    private BigDecimal salary;
    private EmploymentStatus status;
    private String profilePictureUrl;
    private String presignedRequestUrl;
}
