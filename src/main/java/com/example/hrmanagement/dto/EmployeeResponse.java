package com.example.hrmanagement.dto;

import com.example.hrmanagement.models.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private Long employeeId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private String email;
    private String phoneNumber;
    private String departmentName;
    private String positionName;
    private BigDecimal salary;
    private String status;
    private String profilePictureUrl;
    private String presignedRequestUrl;

    public static EmployeeResponse fromEntity(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setEmployeeId(employee.getEmployeeId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setGender(employee.getGender() != null ? employee.getGender().name() : null);
        response.setBirthDate(employee.getBirthDate());
        response.setHireDate(employee.getHireDate());
        response.setEmail(employee.getEmail());
        response.setPhoneNumber(employee.getPhoneNumber());
        response.setDepartmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null);
        response.setPositionName(employee.getPosition() != null ? employee.getPosition().getTitle() : null);
        response.setSalary(employee.getSalary());
        response.setStatus(employee.getStatus() != null ? employee.getStatus().name() : null);
        response.setProfilePictureUrl(employee.getProfilePictureUrl());
        return response;
    }
}
