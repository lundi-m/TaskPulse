package com.lundi_m.taskpulse.recommendation;

import com.lundi_m.taskpulse.model.entity.MoodEntry;
import com.lundi_m.taskpulse.model.entity.Task;
import com.lundi_m.taskpulse.model.enums.DifficultyLevel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class ScoringEngine {

    // Weights
    private static final double MOOD_COMPATIBILITY_WEIGHT = 0.4;
    private static final double ENERGY_COMPATIBILITY_WEIGHT = 0.25;
    private static final double URGENCY_WEIGHT = 0.2;
    private static final double TIME_FIT_WEIGHT = 0.15;

    public Optional<RecommendationScore> recommend(List<Task> tasks, MoodEntry mood){
        return tasks.stream()
                .filter(this::isNotCompleted)
                .map(task -> calculateRecommendation(task, mood))
                .max(Comparator.comparingDouble(RecommendationScore::getScore));
    }

    public List<RecommendationScore> rankAll(List<Task> tasks, MoodEntry mood){
        return tasks.stream()
                .filter(this::isNotCompleted)
                .map(task -> calculateRecommendation(task, mood))
                .sorted(Comparator.comparingDouble(RecommendationScore::getScore).reversed())
                .toList();
    }

    public RecommendationScore calculateRecommendation(Task task, MoodEntry mood){

        double moodScore = calculateMoodCompatibility(task, mood);
        double energyScore = calculateEnergyCompatibility(task, mood);
        double urgencyScore = calculateUrgency(task);
        double timeScore = calculateTimeFit(task, mood);

        double finalScore = (moodScore * MOOD_COMPATIBILITY_WEIGHT)
                + (energyScore * ENERGY_COMPATIBILITY_WEIGHT)
                + (urgencyScore * URGENCY_WEIGHT)
                + (timeScore * TIME_FIT_WEIGHT);

        return new RecommendationScore(
                task,
                finalScore,
                moodScore,
                energyScore,
                urgencyScore,
                timeScore
        );
    }

    // Get tasks that are not completed
    private boolean isNotCompleted(Task task){
        return !task.getCompleted().equals("Completed");
    }

    // Factor calculations

    private double calculateUrgency(Task task){
        if (task.getDeadline() == null) return 1.0;

        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadline());

        if (remainingDays <= 0)  return 5.0;
        if (remainingDays <= 2)  return 4.0;
        if (remainingDays <= 7)  return 3.0;
        if (remainingDays <= 30) return 2.0;

        return 1.0;
    }

    private double calculateMoodCompatibility(Task task, MoodEntry mood) {

        DifficultyLevel difficulty = task.getDifficultyLevel();

        return switch (mood.getMoodType()) {

            case EXHAUSTED -> switch (difficulty) {
                case VERY_EASY -> 5.0;
                case EASY -> 4.5;
                case MEDIUM -> 4.0;
                case HARD -> 2.5;
                case VERY_HARD -> 1.0;
            };

            case LOW -> switch (difficulty) {
                case VERY_EASY -> 4.5;
                case EASY -> 5.0;
                case MEDIUM -> 4.0;
                case HARD -> 2.5;
                case VERY_HARD -> 1.5;
            };

            case NEUTRAL -> switch (difficulty) {
                case VERY_EASY, VERY_HARD -> 3.0;
                case EASY, HARD -> 4.0;
                case MEDIUM -> 5.0;
            };

            case FOCUSED -> switch (difficulty) {
                case VERY_EASY -> 2.0;
                case EASY -> 3.0;
                case MEDIUM -> 4.0;
                case HARD -> 5.0;
                case VERY_HARD -> 4.5;
            };

            case ENERGIZED -> switch (difficulty) {
                case VERY_EASY -> 1.5;
                case EASY -> 2.5;
                case MEDIUM -> 4.0;
                case HARD -> 4.5;
                case VERY_HARD -> 5.0;
            };
        };
    }

    private double calculateTimeFit(Task task, MoodEntry mood) {
        int availableTime = mood.getAvailableTime();
        int estimateTime = task.getEstimatedDuration();

        double ratio = (double) availableTime / estimateTime;

        if (ratio >= 1.0) return 5.0;
        if (ratio >= 0.8) return 4.0;
        if (ratio >= 0.6) return 3.0;
        if (ratio >= 0.4) return 2.0;

        return 1.0;
    }

    private double calculateEnergyCompatibility(Task task, MoodEntry mood){

        int userEnergy = mood.getEnergyLevel().getValue();
        int taskDifficulty = task.getDifficultyLevel().getValue();

        int gap = Math.abs(userEnergy - taskDifficulty);

        return switch (gap){
            case 0 -> 5.0;
            case 1 -> 4.0;
            case 2 -> 3.0;
            case 3 -> 2.0;

            default -> 1.0;
        };
    }
}



