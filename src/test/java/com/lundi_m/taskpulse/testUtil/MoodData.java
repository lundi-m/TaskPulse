package com.lundi_m.taskpulse.testUtil;

import com.lundi_m.taskpulse.dto.mood.MoodRequest;
import com.lundi_m.taskpulse.dto.mood.MoodResponse;
import com.lundi_m.taskpulse.model.entity.MoodEntry;
import com.lundi_m.taskpulse.model.enums.EnergyLevel;
import com.lundi_m.taskpulse.model.enums.MoodType;

import java.time.Instant;

public class MoodData {

    public static MoodEntry createMood(MoodType moodType, EnergyLevel energyLevel, int availableTime){
        return  MoodEntry.builder()
                .moodType(moodType)
                .energyLevel(energyLevel)
                .availableTime(availableTime)
                .build();
    }


    public static MoodRequest createMoodRequest(){
        MoodRequest request = new MoodRequest();

        request.setMoodType(MoodType.NEUTRAL);
        request.setEnergyLevel(EnergyLevel.MODERATE);
        request.setAvailableTime(90);

        return request;
    }

    public static MoodResponse createMoodResponse(){
        return MoodResponse.builder()
                .moodType(MoodType.NEUTRAL)
                .energyLevel(EnergyLevel.MODERATE)
                .availableTime(90)
                .recordedAt(Instant.now())
                .build();
    }

    public static MoodResponse createMoodResponse2(){
        return MoodResponse.builder()
                .moodType(MoodType.EXHAUSTED)
                .energyLevel(EnergyLevel.LOW)
                .availableTime(30)
                .recordedAt(Instant.now())
                .build();
    }
}
