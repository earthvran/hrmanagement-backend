package com.example.hrmanagement.service;

import com.example.hrmanagement.dto.DepartmentRequest;
import com.example.hrmanagement.dto.DepartmentResponse;
import com.example.hrmanagement.models.Department;
import com.example.hrmanagement.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    // CREATE
    public void createDepartment(DepartmentRequest request) {
        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        departmentRepository.save(department);
    }
    // GET ALL
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAllByOrderByDepartmentIdAsc().stream()
                .map(DepartmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return DepartmentResponse.fromEntity(department);
    }

    // UPDATE
    public void updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        departmentRepository.save(department);
    }

    // DELETE
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found");
        }
        departmentRepository.deleteById(id);
    }
}
