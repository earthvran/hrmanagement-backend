package com.example.hrmanagement.repository;

import com.example.hrmanagement.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
    List<Department> findAllByOrderByDepartmentIdAsc();
}
