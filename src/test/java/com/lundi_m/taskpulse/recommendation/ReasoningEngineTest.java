package com.lundi_m.taskpulse.recommendation;

import com.lundi_m.taskpulse.model.entity.MoodEntry;
import com.lundi_m.taskpulse.model.entity.Task;
import com.lundi_m.taskpulse.model.enums.DifficultyLevel;
import com.lundi_m.taskpulse.model.enums.EnergyLevel;
import com.lundi_m.taskpulse.model.enums.MoodType;
import com.lundi_m.taskpulse.model.enums.Priority;
import com.lundi_m.taskpulse.testUtil.MoodData;
import com.lundi_m.taskpulse.testUtil.TaskData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReasoningEngineTest {

    private ReasoningEngine reasoningEngine;
    private ScoringEngine scoringEngine;

    @BeforeEach
    void setUp(){
        reasoningEngine = new ReasoningEngine();
        scoringEngine = new ScoringEngine();
    }

    @Test
    void shouldGenerateExcellentRecommendationReasoning(){

        MoodEntry mood = MoodData.createMood(MoodType.FOCUSED, EnergyLevel.HIGH, 120);

        Task task = TaskData.createTask("Study",
                DifficultyLevel.HARD,
                Priority.HIGH,
                60,
                LocalDate.now().plusDays(1));

        Task task2 = TaskData.createTask("Read emails",
                DifficultyLevel.EASY,
                Priority.HIGH,
                20,
                LocalDate.now().plusDays(2));

        RecommendationScore score  =  scoringEngine
                .recommend(List.of(task, task2), mood)
                .orElseThrow();

        String reasoning = reasoningEngine.buildRecommendationReasoning(score, mood);

        assertEquals("Study", score.getTask().getTitle());
        assertTrue(reasoning.contains("excellent recommendation"));
        assertTrue(reasoning.contains("focused"));
        assertTrue(reasoning.contains("high"));
    }

    @Test
    void shouldMentionUpcomingDeadline(){

        MoodEntry mood = MoodData.createMood(MoodType.FOCUSED, EnergyLevel.MODERATE, 90);

        Task task = TaskData.createTask("Study",
                DifficultyLevel.MEDIUM,
                Priority.URGENT,
                60,
                LocalDate.now().plusDays(7));

        Task task2 = TaskData.createTask("Assignment",
                DifficultyLevel.MEDIUM,
                Priority.HIGH,
                90,
                LocalDate.now().plusDays(1));

        RecommendationScore score  =  scoringEngine
                .recommend(List.of(task, task2), mood)
                .orElseThrow();

        String reasoning = reasoningEngine.buildRecommendationReasoning(score, mood);

        assertEquals("Assignment", score.getTask().getTitle());
        assertTrue(reasoning.contains("deadline"));
    }

    @Test
    void shouldMentionEnoughAvailableTime(){

        MoodEntry mood = MoodData.createMood(MoodType.NEUTRAL, EnergyLevel.MODERATE, 90);

        Task task = TaskData.createTask("Yoga",
                DifficultyLevel.MEDIUM,
                Priority.MEDIUM,
                60,
                null);

        Task task2 = TaskData.createTask("Study",
                DifficultyLevel.MEDIUM,
                Priority.MEDIUM,
                100,
                null);

        RecommendationScore score  =  scoringEngine
                .recommend(List.of(task, task2), mood)
                .orElseThrow();

        String reason = reasoningEngine.buildRecommendationReasoning(score, mood);

        assertEquals("Yoga", score.getTask().getTitle());
        assertTrue(reason.contains("available time"));
    }

    @Test
    void shouldMentionLimitedAvailableTime(){

        MoodEntry mood = MoodData.createMood(MoodType.LOW, EnergyLevel.LOW, 30);

        Task task = TaskData.createTask("Project",
                DifficultyLevel.VERY_EASY,
                Priority.MEDIUM,
                90,
                null);

        Task task2 = TaskData.createTask("Gym",
                DifficultyLevel.HARD,
                Priority.MEDIUM,
                100,
                null);

        RecommendationScore score  =  scoringEngine
                .recommend(List.of(task, task2), mood)
                .orElseThrow();

        String reason = reasoningEngine.buildRecommendationReasoning(score, mood);

        assertEquals("Project", score.getTask().getTitle());
        assertTrue(reason.contains("You may not finish it in one session"));
    }
}
