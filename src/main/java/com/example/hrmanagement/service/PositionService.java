package com.example.hrmanagement.service;

import com.example.hrmanagement.dto.PositionResponse;
import com.example.hrmanagement.models.Department;
import com.example.hrmanagement.models.Position;
import com.example.hrmanagement.dto.PositionRequest;
import com.example.hrmanagement.repository.DepartmentRepository;
import com.example.hrmanagement.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;

    public void createPosition(PositionRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Position position = new Position();
        position.setTitle(request.getTitle());
        position.setLevel(request.getLevel());
        position.setDescription(request.getDescription());
        position.setDepartment(department);

        positionRepository.save(position);
    }

    public List<PositionResponse> getAllPositions() {
        return positionRepository.findAll()
                .stream()
                .map(PositionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public PositionResponse getPositionById(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        return PositionResponse.fromEntity(position);
    }

    public List<PositionResponse> getPositionsByDepartmentId(Long departmentId) {
        List<Position> positions = positionRepository.findByDepartment_DepartmentId(departmentId);
        return positions.stream()
                .map(PositionResponse::fromEntity)
                .toList();
    }

    public void updatePosition(Long id, PositionRequest request) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        position.setTitle(request.getTitle());
        position.setLevel(request.getLevel());
        position.setDescription(request.getDescription());
        position.setDepartment(department);

        positionRepository.save(position);
    }

    public void deletePosition(Long id) {
        if (!positionRepository.existsById(id)) {
            throw new RuntimeException("Position not found");
        }
        positionRepository.deleteById(id);
    }
}
