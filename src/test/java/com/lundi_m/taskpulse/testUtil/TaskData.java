package com.lundi_m.taskpulse.testUtil;

import com.lundi_m.taskpulse.model.entity.Task;
import com.lundi_m.taskpulse.model.enums.DifficultyLevel;
import com.lundi_m.taskpulse.model.enums.Priority;

import java.time.LocalDate;

public class TaskData {

    public static Task createTask(String title,
                            DifficultyLevel difficulty,
                            Priority priority,
                            int estimatedDuration,
                            LocalDate deadline) {
        return Task.builder()
                .title(title)
                .difficultyLevel(difficulty)
                .priority(priority)
                .estimatedDuration(estimatedDuration)
                .deadline(deadline)
                .completed("Not completed")
                .build();
    }
}
