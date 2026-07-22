package com.lundi_m.taskpulse.testUtil;

import com.lundi_m.taskpulse.model.entity.MoodEntry;
import com.lundi_m.taskpulse.model.enums.EnergyLevel;
import com.lundi_m.taskpulse.model.enums.MoodType;

public class MoodData {

    public static MoodEntry createMood(MoodType moodType, EnergyLevel energyLevel, int availableTime){
        return  MoodEntry.builder()
                .moodType(moodType)
                .energyLevel(energyLevel)
                .availableTime(availableTime)
                .build();
    }
}
