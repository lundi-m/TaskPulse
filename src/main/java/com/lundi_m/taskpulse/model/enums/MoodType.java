package com.lundi_m.taskpulse.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MoodType {
    EXHAUSTED(1),
    LOW(2),
    NEUTRAL(3),
    FOCUSED(4),
    ENERGIZED(5);
    private final int value;
}
