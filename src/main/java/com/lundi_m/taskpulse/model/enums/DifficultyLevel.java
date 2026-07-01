package com.lundi_m.taskpulse.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DifficultyLevel {
    VERY_EASY(1),
    EASY(2),
    MEDIUM(3),
    HARD(4),
    VERY_HARD(5);

    private final int value;

}
