package com.example.hrmanagement.controller;

import com.example.hrmanagement.dto.PositionResponse;
import com.example.hrmanagement.dto.PositionRequest;
import com.example.hrmanagement.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody PositionRequest request) {
        positionService.createPosition(request);
        return ResponseEntity.ok("Position created successfully");
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PositionResponse>> getAll() {
        return ResponseEntity.ok(positionService.getAllPositions());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<PositionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(positionService.getPositionById(id));
    }

    @GetMapping("/getByDepartmentId/{departmentId}")
    public List<PositionResponse> getPositionsByDepartment(@PathVariable Long departmentId) {
        return positionService.getPositionsByDepartmentId(departmentId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody PositionRequest request) {
        positionService.updatePosition(id, request);
        return ResponseEntity.ok("Position updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        positionService.deletePosition(id);
        return ResponseEntity.ok("Position deleted successfully");
    }
}

