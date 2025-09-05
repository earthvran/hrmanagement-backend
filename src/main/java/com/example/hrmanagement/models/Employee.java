package com.example.hrmanagement.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String employeeCode;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;
    private LocalDate hireDate;

    private String email;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    private Department department;

    @ManyToOne
    @JoinColumn(name = "position_id")
    @ToString.Exclude
    private Position position;

    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus status;

    @OneToOne(mappedBy = "employee")
    @ToString.Exclude
    private User user;

    private String profilePictureUrl;
}

