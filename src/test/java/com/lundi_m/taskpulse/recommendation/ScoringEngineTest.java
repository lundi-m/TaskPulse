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

public class ScoringEngineTest {

    private ScoringEngine scoringEngine;

    @BeforeEach
    void setUp(){
        scoringEngine = new ScoringEngine();
    }

    @Test
    void shouldRecommendVeryEasyTaskForExhaustedAndDrainedUser(){

        MoodEntry mood = MoodData.createMood(MoodType.EXHAUSTED, EnergyLevel.DRAINED, 60);

        Task easyTask = TaskData.createTask("easy",
                DifficultyLevel.EASY,
                Priority.LOW,
                30,
                null);
        Task veryEasyTask = TaskData.createTask("very easy",
                DifficultyLevel.VERY_EASY,
                Priority.LOW,
                50,
                null);
        Task hardTask = TaskData.createTask("hard",
                DifficultyLevel.HARD,
                Priority.HIGH,
                40,
                null);

        RecommendationScore recommendation = scoringEngine.recommend(List.of(easyTask, veryEasyTask, hardTask), mood)
                .orElseThrow();

        assertEquals("very easy", recommendation.getTask().getTitle());
    }

    @Test
    void shouldRecommendEasyOrMediumTaskForExhaustedAndPeakUser(){

        MoodEntry mood = MoodData.createMood(MoodType.EXHAUSTED, EnergyLevel.PEAK, 60);

        Task easyTask = TaskData.createTask("easy",
                DifficultyLevel.EASY,
                Priority.LOW,
                30,
                null);
        Task veryEasyTask = TaskData.createTask("very easy",
                DifficultyLevel.VERY_EASY,
                Priority.LOW,
                50,
                null);
        Task mediumTask = TaskData.createTask("medium",
                DifficultyLevel.MEDIUM,
                Priority.LOW,
                29,
                null);
        Task hardTask = TaskData.createTask("hard",
                DifficultyLevel.HARD,
                Priority.HIGH,
                40,
                null);
        Task veryHardTask = TaskData.createTask("hard",
                DifficultyLevel.VERY_HARD,
                Priority.MEDIUM,
                45,
                null);

        RecommendationScore recommendation = scoringEngine
                .recommend(List.of(veryEasyTask, easyTask, mediumTask, veryHardTask, hardTask), mood)
                .orElseThrow();

        assertEquals("medium", recommendation.getTask().getTitle());
    }

    @Test
    void shouldRecommendVeryHardTaskForEnergizedAndPeakUser(){

        MoodEntry mood = MoodData.createMood(MoodType.ENERGIZED, EnergyLevel.PEAK, 60);

        Task easyTask = TaskData.createTask("easy",
                DifficultyLevel.EASY,
                Priority.LOW,
                30,
                null);
        Task veryEasyTask = TaskData.createTask("very easy",
                DifficultyLevel.VERY_EASY,
                Priority.LOW,
                50,
                null);
        Task mediumTask = TaskData.createTask("medium",
                DifficultyLevel.MEDIUM,
                Priority.LOW,
                29,
                null);
        Task hardTask = TaskData.createTask("hard",
                DifficultyLevel.HARD,
                Priority.HIGH, 40,
                null);
        Task veryHardTask = TaskData.createTask("very hard",
                DifficultyLevel.VERY_HARD,
                Priority.MEDIUM, 45,
                null);

        RecommendationScore recommendation = scoringEngine
                .recommend(List.of(veryEasyTask, easyTask, mediumTask, veryHardTask, hardTask), mood)
                .orElseThrow();

        assertEquals("very hard", recommendation.getTask().getTitle());
    }

    @Test
    void shouldRankTasksInDescendingOrder(){

        MoodEntry mood = MoodData.createMood(MoodType.ENERGIZED, EnergyLevel.HIGH, 120);

        Task mediumTask = TaskData.createTask("medium",
                DifficultyLevel.MEDIUM,
                Priority.HIGH,
                100,
                null);
        Task hardTask = TaskData.createTask("hard",
                DifficultyLevel.HARD, Priority.LOW,
                67,
                null);
        Task veryHardTask = TaskData.createTask("very hard",
                DifficultyLevel.VERY_HARD,
                Priority.MEDIUM,
                98,
                null);

        List<RecommendationScore> scores = scoringEngine.rankAll(List.of(hardTask, mediumTask, veryHardTask), mood);

        assertTrue(scores.get(0).getScore() >= scores.get(1).getScore() &
                scores.get(1).getScore() >= scores.get(2).getScore());
    }

    @Test
    void shouldIgnoreCompletedTasks(){

        MoodEntry mood = MoodData.createMood(MoodType.NEUTRAL, EnergyLevel.HIGH, 240);

        Task completed = TaskData.createTask("completed",
                DifficultyLevel.EASY,
                Priority.MEDIUM,
                80,
                null);

        completed.setCompleted("Completed");

        Task notCompleted = TaskData.createTask("not completed",
                DifficultyLevel.HARD,
                Priority.URGENT,
                180,
                null);

        RecommendationScore recommendation = scoringEngine
                .recommend(List.of(completed, notCompleted), mood)
                        .orElseThrow();

        assertEquals("not completed", recommendation.getTask().getTitle());
    }

    @Test
    void shouldPrioritizeUrgentTask(){

        MoodEntry mood = MoodData.createMood(MoodType.FOCUSED, EnergyLevel.HIGH, 180);

        Task urgentTask = TaskData.createTask("urgent",
                DifficultyLevel.MEDIUM,
                Priority.URGENT,
                120,
                LocalDate.now().plusDays(7));

        Task normalTask = TaskData.createTask("normal",
                DifficultyLevel.EASY,
                Priority.MEDIUM,
                120,
                LocalDate.now().plusDays(4));

        RecommendationScore recommend = scoringEngine
                .recommend(List.of(urgentTask, normalTask), mood)
                .orElseThrow();

        assertEquals("urgent", recommend.getTask().getTitle());
    }
}
