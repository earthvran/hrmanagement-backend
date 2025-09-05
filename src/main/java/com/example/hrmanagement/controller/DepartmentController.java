package com.example.hrmanagement.controller;

import com.example.hrmanagement.dto.DepartmentRequest;
import com.example.hrmanagement.dto.DepartmentResponse;
import com.example.hrmanagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/createDepartment")
    public ResponseEntity<String> createDepartment(@RequestBody DepartmentRequest request) {
        departmentService.createDepartment(request);
        return ResponseEntity.ok("Department created successfully");
    }

    @GetMapping("/getAllDepartments")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/getDepartmentById/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @PutMapping("/updateDepartment/{id}")
    public ResponseEntity<String> updateDepartment(@PathVariable Long id, @RequestBody DepartmentRequest request) {
        departmentService.updateDepartment(id, request);
        return ResponseEntity.ok("Department updated");
    }

    @DeleteMapping("/deleteDepartment/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Department deleted");
    }
}

