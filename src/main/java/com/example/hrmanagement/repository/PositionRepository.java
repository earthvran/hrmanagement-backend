package com.example.hrmanagement.repository;

import com.example.hrmanagement.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByTitle(String title); // ค้นหาตำแหน่งตามชื่อ
    List<Position> findByLevel(String level); // ค้นหาตำแหน่งทั้งหมดในระดับเดียวกัน
    List<Position> findByDepartment_DepartmentId(Long departmentId); // ค้นหาตำแหน่งตามแผนก
}
