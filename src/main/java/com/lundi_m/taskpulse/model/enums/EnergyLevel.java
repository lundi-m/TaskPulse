package com.lundi_m.taskpulse.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnergyLevel {
    DRAINED(1),
    LOW(2),
    MODERATE(3),
    HIGH(4),
    PEAK(5);

    private final int value;
}
