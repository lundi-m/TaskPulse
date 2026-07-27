package com.lundi_m.taskpulse.recommendation;

import com.lundi_m.taskpulse.model.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RecommendationScore {
    private final Task task;

    private final double score;

    private final double moodScore;
    private final double energyScore;
    private final double urgencyScore;
    private final double timeScore;

}
