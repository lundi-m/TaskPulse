package com.lundi_m.taskpulse.testUtil;

import com.lundi_m.taskpulse.dto.recommendation.RecommendationResponse;
import com.lundi_m.taskpulse.model.enums.DifficultyLevel;
import com.lundi_m.taskpulse.model.enums.Priority;
import com.lundi_m.taskpulse.recommendation.RecommendationScore;

import java.time.LocalDate;

public class RecommendationData {

    public static RecommendationResponse createResponse(){
        return RecommendationResponse.builder()
                .taskId(1L)
                .title("Assignment")
                .description("Finish and Submit the assignment before the due date")
                .priority(Priority.HIGH)
                .difficultyLevel(DifficultyLevel.MEDIUM)
                .estimatedDuration(90)
                .deadline(LocalDate.now().plusDays(3))
                .score(4.6)
                .reasoning("This is a strong recommendation. " +
                        "You are currently feeling exhausted with moderate energy." +
                        " The task strongly matches your current mood." +
                        " Your energy should comfortably handle this task." +
                        " It fits comfortably within your available time.")
                .build();
    }

    public static RecommendationResponse createResponse2(){
        return RecommendationResponse.builder()
                .taskId(1L)
                .title("Maintenance")
                .description("Fix the roof")
                .priority(Priority.HIGH)
                .difficultyLevel(DifficultyLevel.HARD)
                .estimatedDuration(180)
                .deadline(LocalDate.now().plusDays(7))
                .score(1.54)
                .reasoning("This task is less suited to your current state." +
                        " You are currently feeling exhausted with moderate energy." +
                        " The task may feel more demanding than your current mood suggests." +
                        " The task is manageable with your current energy." +
                        " You may not finish it in one session, but making progress is worthwhile.")
                .build();
    }

}
