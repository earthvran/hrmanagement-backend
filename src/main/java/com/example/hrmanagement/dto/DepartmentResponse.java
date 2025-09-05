package com.example.hrmanagement.dto;

import com.example.hrmanagement.models.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse {
    private Long departmentId;
    private String name;
    private String description;

    public static DepartmentResponse fromEntity(Department department) {
        return new DepartmentResponse(
                department.getDepartmentId(),
                department.getName(),
                department.getDescription()
        );
    }
}
