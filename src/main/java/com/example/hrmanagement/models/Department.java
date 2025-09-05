package com.example.hrmanagement.models;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}

