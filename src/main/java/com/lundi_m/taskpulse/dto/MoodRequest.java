package com.lundi_m.taskpulse.dto;

import com.lundi_m.taskpulse.model.enums.EnergyLevel;
import com.lundi_m.taskpulse.model.enums.MoodType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MoodRequest {

    @NotNull(message = "Mood type is required")
    private MoodType moodType;

    @NotNull(message = "Energy level is required")
    private EnergyLevel energyLevel;

    @NotNull(message = "Available time is required")
    @Min(value = 5, message = "Minimum available time must be at least 5 minutes")
    @Max(value = 1440, message = "Maximum available time is 1440 minutes (24 Hours)")
    private Integer availableTime;
}
