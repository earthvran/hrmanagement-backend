package com.example.hrmanagement.dto;

import lombok.Data;

@Data
public class PositionRequest {
    private String title;
    private String level;
    private String description;
    private Long departmentId;
}
