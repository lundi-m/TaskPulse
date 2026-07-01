package com.lundi_m.taskpulse.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lundi_m.taskpulse.model.enums.DifficultyLevel;
import com.lundi_m.taskpulse.model.enums.Priority;
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
        "createdAt",
        "completedAt"
})
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Integer estimatedDuration;
    private DifficultyLevel difficultyLevel;
    private LocalDate deadline;
    private String completed;
    private Instant createdAt;
    private Instant completedAt;
}
