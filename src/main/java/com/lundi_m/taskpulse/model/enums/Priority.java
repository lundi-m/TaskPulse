package com.lundi_m.taskpulse.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Priority{
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    URGENT(4),
    CRITICAL(5);

    private final int value;
}
