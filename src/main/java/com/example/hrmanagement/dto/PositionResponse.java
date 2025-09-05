package com.example.hrmanagement.dto;

import com.example.hrmanagement.models.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionResponse {
    private Long positionId;
    private String title;
    private String level;
    private String description;
    private String departmentName;

    public static PositionResponse fromEntity(Position position) {
        return new PositionResponse(
                position.getPositionId(),
                position.getTitle(),
                position.getLevel(),
                position.getDescription(),
                position.getDepartment() != null ? position.getDepartment().getName() : null
        );
    }
}

