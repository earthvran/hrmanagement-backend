package com.example.hrmanagement.controller;

import com.example.hrmanagement.dto.EmployeeRequest;
import com.example.hrmanagement.dto.EmployeeResponse;
import com.example.hrmanagement.service.EmployeeService;
import com.example.hrmanagement.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final StorageService storageService;
    @PostMapping(value = "/createEmployee", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createEmployee(
            @RequestPart("request") EmployeeRequest request,
            @RequestPart("file") MultipartFile file) {
        employeeService.insertEmployee(request, file);
        return ResponseEntity.ok("Employee created successfully with profile picture");
    }

    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/getEmployeeById/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
    // PUT: Update Employee
    @PutMapping(value = "/updateEmployee/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @RequestPart("request") EmployeeRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request, file));
    }

    // DELETE: Delete Employee
    @DeleteMapping("/deleteEmployee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
