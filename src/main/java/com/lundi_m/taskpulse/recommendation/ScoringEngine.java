package com.lundi_m.taskpulse.recommendation;

import com.lundi_m.taskpulse.model.entity.MoodEntry;
import com.lundi_m.taskpulse.model.entity.Task;
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
    private static final double URGENCY_WEIGHT = 0.3;
    private static final double TIME_FIT_WEIGHT = 0.2;
    private static final double DIFFICULTY_WEIGHT = 0.1;

    public Optional<RecommendationScore> recommend(List<Task> tasks, MoodEntry mood){
        return tasks.stream()
                .filter(this::isNotCompleted)
                .map(task -> new RecommendationScore(task, calculateScore(task, mood)))
                .max(Comparator.comparingDouble(RecommendationScore::getScore));
    }

    public List<RecommendationScore> rankAll(List<Task> tasks, MoodEntry mood){
        return tasks.stream()
                .filter(this::isNotCompleted)
                .map(task -> new RecommendationScore(task, calculateScore(task, mood)))
                .sorted(Comparator.comparingDouble(RecommendationScore::getScore).reversed())
                .toList();
    }

    private double calculateScore(Task task, MoodEntry mood){

        double urgency = calculateUrgency(task);
        double moodCompatibility = calculateMoodCompatibility(task, mood);
        double timeFit = calculateTimeFit(task, mood);
        double difficulty = task.getDifficultyLevel().getValue();


        return (urgency * URGENCY_WEIGHT)
                + (moodCompatibility * MOOD_COMPATIBILITY_WEIGHT)
                + (timeFit * TIME_FIT_WEIGHT)
                + (difficulty * DIFFICULTY_WEIGHT);

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

    private double calculateMoodCompatibility(Task task, MoodEntry mood){
        double userScore = (mood.getMoodType().getValue() + mood.getEnergyLevel().getValue());

        double taskPriority = task.getPriority().getValue();

        // closer the match, higher the compatibility score
        return 5.0 - Math.abs(userScore - taskPriority);
    }

    private double calculateTimeFit(Task task, MoodEntry mood){
        int availableTime = mood.getAvailableTime();
        int estimateTime = task.getEstimatedDuration();

        if (availableTime >= estimateTime)       return 5.0;
        if (availableTime <= estimateTime * 0.8) return 4.0;
        if (availableTime <= estimateTime * 0.6) return 3.0;
        if (availableTime <= estimateTime * 0.4) return 2.0;

        return 1.0;
    }
}


