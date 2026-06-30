package com.lundi_m.taskpulse.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "priority",
        "estimatedDuration",
        "difficultyLevel",
        "deadline",
        "isCompleted",
        "createdAt"
})
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Integer priority;
    private Integer estimatedDuration;
    private Integer difficultyLevel;
    private LocalDate deadline;
    private String completed;
    private Instant createdAt;
}
