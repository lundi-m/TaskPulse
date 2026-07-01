package com.lundi_m.taskpulse.dto;

import com.lundi_m.taskpulse.model.enums.EnergyLevel;
import com.lundi_m.taskpulse.model.enums.MoodType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class MoodResponse {
    private MoodType moodType;
    private EnergyLevel energyLevel;
    private Integer availableTime;
    private Instant recordedAt;
}
