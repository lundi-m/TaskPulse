package com.lundi_m.taskpulse.dto.recommendation;

import com.lundi_m.taskpulse.model.enums.DifficultyLevel;
import com.lundi_m.taskpulse.model.enums.Priority;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RecommendationResponse {
    private Long taskId;
    private String title;
    private String description;
    private Priority priority;
    private DifficultyLevel difficultyLevel;
    private Integer estimatedDuration;
    private LocalDate deadline;
    private double score;
    private String reasoning;
}
