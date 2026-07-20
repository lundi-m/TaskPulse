package com.lundi_m.taskpulse.recommendation;

import com.lundi_m.taskpulse.model.entity.MoodEntry;
import com.lundi_m.taskpulse.model.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class ReasoningEngine {

    public String buildRecommendationReasoning(RecommendationScore recommendationScore, MoodEntry mood) {

        StringBuilder reason = new StringBuilder();

        // Overall recommendation
        appendRecommendationStrength(reason, recommendationScore.getScore());

        // User state
        reason.append("You are currently feeling ")
                .append(mood.getMoodType().name().toLowerCase())
                .append(" with ")
                .append(mood.getEnergyLevel().name().toLowerCase())
                .append(" energy. ");

        // Explain each factor
        appendMoodReason(reason, recommendationScore);
        appendEnergyReason(reason, recommendationScore);
        appendUrgencyReason(reason, recommendationScore);
        appendTimeReason(reason, recommendationScore);

        return reason.toString();
    }

    private void appendMoodReason(StringBuilder reason, RecommendationScore score) {

        if (score.getMoodScore() >= 4.5) {
            reason.append("The task strongly matches your current mood. ");
        } else if (score.getMoodScore() >= 3.5) {
            reason.append("The task is a good match for your current mood. ");
        } else if (score.getMoodScore() >= 2.5) {
            reason.append("The task is a reasonable match for your mood. ");
        } else {
            reason.append("The task may feel more demanding than your current mood suggests. ");
        }
    }

    private void appendEnergyReason(StringBuilder reason, RecommendationScore score) {

        if (score.getEnergyScore() >= 4.5) {
            reason.append("Your energy level is well suited for this task. ");
        } else if (score.getEnergyScore() >= 3.5) {
            reason.append("Your energy should comfortably handle this task. ");
        } else if (score.getEnergyScore() >= 2.5) {
            reason.append("The task is manageable with your current energy. ");
        } else {
            reason.append("Your current energy may make this task more challenging. ");
        }
    }


    private void appendUrgencyReason(StringBuilder reason, RecommendationScore score) {

        Task task = score.getTask();

        if (task.getDeadline() == null) {
            return;
        }

        if (score.getUrgencyScore() >= 5) {
            reason.append("It is overdue or due today and should be completed as soon as possible. ");
        } else if (score.getUrgencyScore() >= 4) {
            reason.append("Its deadline is approaching soon. ");
        } else if (score.getUrgencyScore() >= 3) {
            reason.append("It has a moderate deadline priority. ");
        }
    }

    private void appendTimeReason(StringBuilder reason, RecommendationScore score) {

        if (score.getTimeScore() >= 5) {
            reason.append("It fits comfortably within your available time.");
        } else if (score.getTimeScore() >= 4) {
            reason.append("It should fit within most of your available time.");
        } else if (score.getTimeScore() >= 3) {
            reason.append("You may need to pace yourself to finish it.");
        } else {
            reason.append("You may not finish it in one session, but making progress is worthwhile.");
        }
    }

    private void appendRecommendationStrength(StringBuilder reason,
    double score) {

        if (score >= 4.5) {
            reason.append("This is an excellent recommendation. ");
        } else if (score >= 3.5) {
            reason.append("This is a strong recommendation. ");
        } else if (score >= 2.5) {
            reason.append("This is a reasonable recommendation. ");
        } else {
            reason.append("This task is less suited to your current state. ");
        }
    }
}
