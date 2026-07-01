package com.lundi_m.taskpulse.dto;

import com.lundi_m.taskpulse.model.enums.DifficultyLevel;
import com.lundi_m.taskpulse.model.enums.Priority;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3,max = 100, message = "Title must be between 3 to 100 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @Max(value = 1440, message = "Estimated minutes cannot exceed 1440")
    private Integer estimatedDuration;

    @NotNull(message = "Difficulty level is required")
    private DifficultyLevel difficultyLevel;

    @FutureOrPresent(message = "Deadline cannot be in the past")
    private LocalDate deadline;
}
